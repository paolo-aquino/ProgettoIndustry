import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class CounterDataGenerator {

    public static void main(String[] args) throws Exception{
        String token = "LmCAol9uXABewsNM81OsMkcAcZmGPt829B-StxaYEFWqN1XBLxx1Eo9uLLpYwbZPytvxLtahU0qwpQB180_2gQ==";
        String bucket = "grafana_test";
        String org = "SUPSI";

        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        while (true){

            Point point = Point
                    .measurement("counter")
                    .addField("count", 1)
                    .time(Instant.now(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);

            // random time between counts [5:30)
            int waiting_time = (int) (Math.random() * 25 + 5);
            System.out.print("written count=1\n");

            Thread.sleep(waiting_time * 1000);

        }
    }
}
