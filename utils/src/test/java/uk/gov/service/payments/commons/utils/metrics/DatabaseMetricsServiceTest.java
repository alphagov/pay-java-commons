package uk.gov.service.payments.commons.utils.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DatabaseMetricsServiceTest {
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStatement;
    @Mock
    ResultSet mockResultSet;

    @Test
    public void updateMetricData() throws Throwable {
        MetricRegistry metricRegistry = new MetricRegistry();
        when(mockConnection.prepareStatement("select *, blk_read_time * 1000 as blk_read_time_ns, blk_write_time * 1000 as blk_write_time_ns from pg_stat_database where datname = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        when(mockPreparedStatement.getResultSet()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        try {
            when(mockResultSet.getLong("numbackends")).thenReturn(1L);
        } catch (SQLException ignored) { }
        Map<String,Long> counterMetrics = Map.ofEntries(
                entry("xact_commit", 2L),
                entry("xact_rollback", 3L),
                entry("blks_read", 4L),
                entry("blks_hit", 5L),
                entry("tup_returned", 6L),
                entry("tup_fetched", 7L),
                entry("tup_inserted", 8L),
                entry("tup_updated", 9L),
                entry("tup_deleted", 10L),
                entry("conflicts", 11L),
                entry("temp_files", 12L),
                entry("temp_bytes", 13L),
                entry("deadlocks", 14L),
                entry("blk_read_time_ns", 15L),
                entry("blk_write_time_ns", 16L)
        );
        counterMetrics.forEach((name, value) -> {
            try {
                when(mockResultSet.getLong(name)).thenReturn(value);
            } catch (SQLException ignored) { }
        });
        DatabaseMetricsService databaseMetricsService = new DatabaseMetricsService(new MockJDBCConnectionFactory(), metricRegistry, "testme");

        // Metrics all default to 0 and unhealthy
        assertEquals(Integer.valueOf(0), ((Gauge<Integer>)metricRegistry.getGauges().get("testmedb.stats_healthy")).getValue());
        assertEquals(Long.valueOf(0), ((Gauge<Long>)metricRegistry.getGauges().get("testmedb.numbackends")).getValue());
        counterMetrics.keySet().forEach(name -> assertEquals(0L, metricRegistry.getCounters().get("testmedb." + name).getCount()));

        databaseMetricsService.updateMetricData();

        Map gauges = metricRegistry.getGauges();
        Map<String, Counter> counters = metricRegistry.getCounters();
        assertEquals(2, gauges.size());
        assertEquals(Integer.valueOf(1), ((Gauge<Integer>)gauges.get("testmedb.stats_healthy")).getValue());
        assertEquals(Long.valueOf(1), ((Gauge<Long>)gauges.get("testmedb.numbackends")).getValue());
        assertEquals(15, counters.size());
        counters.forEach((name, counter) -> assertEquals(counterMetrics.get(name.substring("testmedb.".length())).longValue(), counter.getCount()));
    }

    private class MockJDBCConnectionFactory implements DatabaseMetricsService.JDBCConnectionProvider{
        @Override
        public Connection getConnection() {
            return mockConnection;
        }
    }
}
