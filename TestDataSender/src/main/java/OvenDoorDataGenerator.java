import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;


public class OvenDoorDataGenerator {

    public static void main(String[] args) throws Exception{
        String token = "LmCAol9uXABewsNM81OsMkcAcZmGPt829B-StxaYEFWqN1XBLxx1Eo9uLLpYwbZPytvxLtahU0qwpQB180_2gQ==";
        String bucket = "grafana_test";
        String org = "SUPSI";

        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        while (true){

            // 10 % probability that the data point is not valid
            double rnd = Math.random();
            boolean is_okay = rnd >= 0.1;

            Point point = Point
                    .measurement("oven_door")
                    .addField("is_okay", is_okay)
                    .time(Instant.now(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);

            Thread.sleep(10*1000);
        }
    }
}
