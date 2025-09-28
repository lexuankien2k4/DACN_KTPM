package com.Nhom7.DACN_KTPM;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

// Lớp chứa dữ liệu, tên trường khớp với Entity
class CarVariantData {
    Integer length, width, height, wheelbase, groundClearance;
    Integer maxPower, maxTorque, rangeNedc, wheelSize;
    String drivetrain, driveModes, chargeTimeDetail, suspensionSystem, brakeSystem;
    String frontLightType, trunkAccess, airConditioningSystem, speakerSystem, driverSeatAdjustment;
    BigDecimal battery, entertainmentScreen;
}
public class VinfastCrawler {

    private static final String URL_TO_CRAWL = "https://shop.vinfastauto.com/vn_vi/ebus.html";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Vinfast_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    public static void main(String[] args) {
        System.out.println("Starting crawl process for URL: " + URL_TO_CRAWL);
        CarVariantData carData = crawlData();
        if (carData != null) {
            saveToDatabase(carData);
        } else {
            System.out.println("No data crawled or an error occurred.");
        }
    }

    public static CarVariantData crawlData() {
        CarVariantData car = new CarVariantData();
        try {
            Document doc = Jsoup.connect(URL_TO_CRAWL).get();
            Elements parameterItems = doc.select("section.homePageVF5-spec .parameter-item");

            for (Element item : parameterItems) {
                String label = item.select("label").text().trim();
                String value = item.select("span").text().trim();

                switch (label) {
                    case "Dài x rộng x Cao (mm)":
                        String[] dimensions = value.split("x");
                        if (dimensions.length == 3) {
                            car.length = parseInteger(dimensions[0]);
                            car.width = parseInteger(dimensions[1]);
                            car.height = parseInteger(dimensions[2]);
                        }
                        break;
                    case "Chiều dài cơ sở": car.wheelbase = parseInteger(value); break;
                    case "Khoảng sáng gầm xe": car.groundClearance = parseInteger(value); break;
                    case "Công suất tối đa": car.maxPower = parseInteger(value); break;
                    case "Mô men xoắn cực đại": car.maxTorque = parseInteger(value); break;
                    case "Quãng đường chạy (NEDC)": car.rangeNedc = parseInteger(value); break;
                    case "Dung lượng pin khả dụng": car.battery = parseBigDecimal(value); break;
                    case "Thời gian nạp pin nhanh nhất (phút)": car.chargeTimeDetail = value; break;
                    case "Dẫn động": car.drivetrain = value; break;
                    case "Chế độ lái": car.driveModes = value; break;
                    case "Hệ thống treo (trước/sau)": car.suspensionSystem = value; break;
                    case "Hệ thống phanh (trước/sau)": car.brakeSystem = value; break;
                    case "Kích thước la-zăng": car.wheelSize = parseInteger(value); break;
                    case "Đèn chiếu sáng phía trước": car.frontLightType = value; break;
                    case "Đóng/mở cốp sau": car.trunkAccess = value; break;
                    case "Hệ thống điều hòa": car.airConditioningSystem = value; break;
                    case "Màn hình giải trí cảm ứng": car.entertainmentScreen = parseBigDecimal(value); break;
                    case "Hệ thống loa": car.speakerSystem = value; break;
                    case "Ghế lái": car.driverSeatAdjustment = value; break;
                }
            }
            System.out.println("Crawled data successfully!");
            return car;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error during crawling process: " + e.getMessage());
            return null;
        }
    }

    public static void saveToDatabase(CarVariantData car) {
        // Câu lệnh INSERT với đầy đủ các cột
        String sql = "INSERT INTO car_variants (" +
                "model_id, name, price, is_active, " + // 4 cột đầu
                "length, width, height, wheelbase, ground_clearance, " +
                "max_power, max_torque, drivetrain, drive_modes, " +
                "battery, range_nedc, charge_time_detail, " +
                "suspension_system, brake_system, wheel_size, " +
                "front_light_type, trunk_access, air_conditioning_system, " +
                "entertainment_screen, speaker_system, driver_seat_adjustment" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 25 tham số

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = conn.prepareStatement(sql);


            statement.setLong(1, 17);
            statement.setString(2, "EBus");
            statement.setLong(3, 1949000000L);
            statement.setBoolean(4, true);

            // --- Gán các giá trị đã crawl được ---
            statement.setObject(5, car.length);
            statement.setObject(6, car.width);
            statement.setObject(7, car.height);
            statement.setObject(8, car.wheelbase);
            statement.setObject(9, car.groundClearance);
            statement.setObject(10, car.maxPower);
            statement.setObject(11, car.maxTorque);
            statement.setString(12, car.drivetrain);
            statement.setString(13, car.driveModes);
            statement.setBigDecimal(14, car.battery);
            statement.setObject(15, car.rangeNedc);
            statement.setString(16, car.chargeTimeDetail);
            statement.setString(17, car.suspensionSystem);
            statement.setString(18, car.brakeSystem);
            statement.setObject(19, car.wheelSize);
            statement.setString(20, car.frontLightType);
            statement.setString(21, car.trunkAccess);
            statement.setString(22, car.airConditioningSystem);
            statement.setBigDecimal(23, car.entertainmentScreen);
            statement.setString(24, car.speakerSystem);
            statement.setString(25, car.driverSeatAdjustment);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new car variant was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving data to database: " + e.getMessage());
        }
    }


    private static Integer parseInteger(String text) {
        if (text == null || text.isEmpty()) return null;
        try { return Integer.parseInt(text.replaceAll("[^\\d.].*", "").trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private static BigDecimal parseBigDecimal(String text) {
        if (text == null || text.isEmpty()) return null;
        try { return new BigDecimal(text.replaceAll("[^\\d.,].*", "").replace(',', '.').trim()); }
        catch (NumberFormatException e) { return null; }
    }
}