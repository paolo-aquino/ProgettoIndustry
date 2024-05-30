import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class RotationSpeedDataGenerator {
    public static void main(String[] args) throws Exception{
        String token = "LmCAol9uXABewsNM81OsMkcAcZmGPt829B-StxaYEFWqN1XBLxx1Eo9uLLpYwbZPytvxLtahU0qwpQB180_2gQ==";
        String bucket = "grafana_test";
        String org = "SUPSI";

        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        while (true){

            // random number of rpm [20, 90)
            double rpm = Math.random() * 70 + 20;
            // with high speed the tag speed_cat is high_speed, otherwise low_speed
            String speed_cat = rpm > 70 ? "high_speed" : "low_speed";

            Point point = Point
                    .measurement("conveyor_speed")
                    .addField("speed", rpm)
                    .addTag("speed_cat", speed_cat)
                    .time(Instant.now(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);
            System.out.printf("written rpm=%f\n", rpm);

            Thread.sleep(30 * 1000);
        }
    }
}
