package com.android.store.db;

import com.android.store.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class handleData {


    private void upProduct() {
        ArrayList<Product> listProduct = new ArrayList<>();
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/274721/OPPO-Reno7-4G-Thumb-cam-1-600x600.jpg", "Điện thoại OPPO Reno7 ", "Samsung Galaxy Note 20 Ultra được chế tác từ những vật liệu cao cấp hàng đầu hiện nay, với sự tỉ mỉ và chất lượng gia công thượng thừa, tạo nên chiếc điện thoại đẹp hơn những gì bạn có thể tưởng tượng. Không chỉ có kiểu dáng thanh lịch, màn hình không viền Infinity-O quyến rũ, Galaxy Note20 Ultra còn thể hiện sự cao cấp ở từng chi tiết nhỏ như các phần viền cạnh sáng bóng, họa tiết phay xước độc đáo trên khung máy, mang đến niềm cảm hứng cho người dùng ở mọi góc cạnh.", "Oppo", 8900000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/246640/xiaomi-redmi-note-11-pro-5g-xam-thumb-600x600.jpg", "Điện thoại Xiaomi Redmi Note 11 Pro 5G", "Xiaomi Redmi Note 11 Pro 5G hội tụ đủ những yếu tố ấn tượng nhất của một chiếc smartphone tầm trung: Camera 108 MP số 1 phân khúc, màn hình AMOLED 120 Hz, pin 5000 mAh, sạc nhanh 67 W, hỗ trợ 5G cùng con chip Snapdragon 695 mạnh mẽ.", "Xiaomi", 8600000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/233241/xiaomi-mi-11-lite-4g-blue-600x600.jpg", "Điện thoại Xiaomi Mi 11 Lite ", "Điện thoại Xiaomi Mi 11 Lite là phiên bản thu gọn của Xiaomi Mi 11 5G được ra mắt trước đó. Thừa hưởng nhiều ưu điểm của đàn anh, Mi 11 Lite hoàn toàn có thể đáp ứng tốt các tác vụ thông thường một cách dễ dàng và đặc biệt hơn máy có thiết kế vô cùng mỏng nhẹ và thời trang.", "Xiaomi", 12000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/229228/xiaomi-redmi-note-10-pro-thumb-xam-600x600-600x600.jpg", "Điện thoại Xiaomi Redmi Note 10 Pro (8GB/128GB)", "Kế thừa và nâng cấp từ thế hệ trước, Xiaomi đã cho ra mắt điện thoại Xiaomi Redmi Note 10 Pro (8GB/128GB) sở hữu một thiết kế cao cấp, sang trọng bên cạnh cấu hình vô cùng mạnh mẽ, hứa hẹn mang đến sự cạnh tranh lớn trong phân khúc smartphone tầm trung.", "xiaomi", 9000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/249720/Vivo-y15s-2021-xanh-den-660x600.jpg", "Điện thoại Vivo Y15s ", "Vivo vừa mang một chiến binh mới đến phân khúc tầm trung giá rẻ có tên Vivo Y15s, một sản phẩm sở hữu khá nhiều ưu điểm như thiết kế đẹp, màn hình lớn, camera kép, pin cực trâu và còn rất nhiều điều thú vị khác đang chờ đón bạn.", "Vivo", 13000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/249945/oppo-a16k-thumb1-600x600-1-600x600.jpg", "Điện thoại Realme C21-Y 3GB", "Realme C21-Y 3 GB là chiếc điện thoại nằm ở phân khúc giá rẻ với điểm nhấn thiết kế hình học sang trọng, bộ 3 camera chất lượng, hiệu năng đáp ứng khá tốt các nhu cầu và thời lượng pin tương đối dài hứa hẹn mang đến cho người dùng những trải nghiệm ấn tượng.", "Realme", 21000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/218355/samsung-galaxy-note-20-062220-122200-fix-600x600.jpg", "Điện thoại Samsung Galaxy Note 20", "Tháng 8/2020, smartphone Galaxy Note 20 chính thức được lên kệ, với thiết kế camera trước nốt ruồi quen thuộc, cụm camera hình chữ nhật mới lạ cùng với vi xử lý Exynos 990 cao cấp của chính hãng điện thoại Samsung chắc hẳn sẽ mang lại một trải nghiệm thú vị cùng hiệu năng mạnh mẽ.", "Samsung", 25000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/229949/TimerThumb/samsung-galaxy-z-flip-3-(6).jpg", "Điện thoại Samsung Galaxy Z Flip3 5G 128GB", "Trong sự kiện Galaxy Unpacked hồi 11/8, Samsung đã chính thức trình làng mẫu điện thoại màn hình gập thế hệ mới mang tên Galaxy Z Flip3 5G 128GB. Đây là một siêu phẩm với màn hình gập dạng vỏ sò cùng nhiều điểm cải tiến và thông số ấn tượng, sản phẩm chắc chắn sẽ thu hút được rất nhiều sự quan tâm của người dùng, đặc biệt là phái nữ.", "samsung", 7000000));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBProduct");
        for(Product product : listProduct) {
            myRef.push().setValue(product);
        }
    }
}
