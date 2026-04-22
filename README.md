### Các vấn đề cần giải quyết sau này: 




#################
# cart 
- Khi đăng nhập xong (khi nhấn mua hàng trong màn cart) đối với user chưa có giỏ hàng thì bị lỗi mất sản phẩm đã chọn để mua 

#################


# 🛒 EasyMart – Cart Module

## 📌 Overview

Cart được thiết kế theo hướng **offline-first**:

* Cập nhật **local ngay lập tức**
* Đồng bộ với server **sau (sync)**

👉 Mục tiêu: UX mượt, không phụ thuộc mạng

---

## 🏗️ Architecture

```
ViewModel → UseCase → Repository → (Room + API)
```

* **Room (local)**: source of truth
* **Remote**: lưu server
* **Repository**: xử lý sync & merge data

---

## ⚙️ Data Flow

### Add / Update

* Lưu vào Room
* `isSynced = false`
* UI update ngay

### Sync

* Gửi các item chưa sync lên server
* Đánh dấu `isSynced = true`
* Fetch remote → merge về local

---

## ⚖️ Trade-offs

**Ưu điểm**

* Mượt, dùng được offline
* Giảm API calls

**Nhược điểm**

* Không real-time 100%
* Phức tạp hơn (sync + conflict)

---

## 🚧 Current

* Manual sync từ ViewModel
* Chưa có auto sync / retry

---

## 🚀 Future Direction

### Option 1 – Real-time sync

* Gọi API mỗi action
  → Đơn giản nhưng tốn API, UX kém khi mạng yếu

### Option 2 – Deferred sync (khuyến nghị)

* Update local → sync sau (debounce / trigger)
  → Cân bằng tốt UX và performance

### Option 3 – Background sync

* Dùng WorkManager
  → Production-level, phức tạp hơn

👉 Hướng dự kiến: **Deferred sync**

---

## 🎯 Conclusion

Cart module:

* Offline-first
* Kiến trúc rõ ràng
* Dễ mở rộng lên production

---

## 🔥 System Thinking

* Local là source of truth
* Sync là async process
* Luôn cân bằng: **UX vs complexity**


#################