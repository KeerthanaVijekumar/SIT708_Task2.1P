# Travel Companion App
### SIT708 - Task 2.1 | Android Development

---

##  About the App

Travel Companion is an Android application designed to help international 
travellers convert essential values quickly and easily. The app supports 
three conversion categories:

-  **Currency** — Convert between USD, AUD, EUR, JPY, and GBP
-  **Fuel & Distance** — Convert between mpg, km/L, Gallons, Liters, 
  Nautical Miles, and Kilometers
-  **Temperature** — Convert between Celsius, Fahrenheit, and Kelvin

---

##  Developer

- **Name:** Keerthana
- **Student ID:** 224719679
- **Unit:** SIT708
- **Task:** Pass Task 2.1

---

##  Built With

| Tool | Details |
|------|---------|
| Language | **Java** (not Kotlin) |
| IDE | Android Studio Panda 3 |
| Minimum SDK | API 24 (Android 7.0 Nougat) |
| Target SDK | API 37 |
| Build System | Gradle with Kotlin DSL |

### Why Java?
This project was built using **Java** rather than Kotlin. Java was chosen 
because it is the language taught in the unit materials and is more 
familiar for beginners learning Android development for the first time. 
Java is fully supported by Android Studio and works identically to Kotlin 
for all features used in this app.

---
---

##  Features Implemented

### Subtask 1 — UI Design
- Dropdown spinner for selecting conversion category
- Dropdown spinners for selecting source and destination units
- Text input field for entering the value to convert
- Convert button to trigger the conversion
- Result display with green background for clear visibility
- Clean, scrollable layout that works on all screen sizes

### Subtask 2 — Conversion Logic
**Currency (Fixed 2026 Rates):**
- 1 USD = 1.55 AUD
- 1 USD = 0.92 EUR
- 1 USD = 148.50 JPY
- 1 USD = 0.78 GBP
- All conversions go through USD as a middle step

**Fuel & Distance:**
- 1 mpg = 0.425 km/L
- 1 Gallon (US) = 3.785 Liters
- 1 Nautical Mile = 1.852 Kilometers

**Temperature:**
- Celsius to Fahrenheit: F = (C × 1.8) + 32
- Fahrenheit to Celsius: C = (F − 32) / 1.8
- Celsius to Kelvin: K = C + 273.15
- All 6 direction combinations are supported

### Subtask 3 — Gemini Nano Research
A 500-word research report on Gemini Nano and on-device AI is included 
in the final submission document. The report covers 5 specific use cases 
for on-device AI to improve privacy and offline functionality.

### Subtask 4 — Validation & Error Handling
-  Empty input shows a Toast error message
-  Non-numeric input (e.g. letters) is caught and shows an error
-  Same unit selected on both sides notifies the user
-  Negative fuel/distance values are rejected
-  Temperature values below absolute zero are rejected

---

##  How to Run the App

### Option 1 — Using a Physical Android Device (Recommended)
1. Enable **Developer Options** on your Android phone:
   - Go to Settings → About Phone
   - Tap **Build Number** 7 times
2. Enable **USB Debugging** in Developer Options
3. Connect your phone via USB cable
4. Set USB mode to **File Transfer**
5. Open the project in Android Studio
6. Select your device from the device dropdown
7. Press the green ** Play** button

### Option 2 — Using Android Emulator
1. Go to **Tools → Device Manager**
2. Create a new virtual device (Medium Phone recommended)
3. Select API 34 or lower for better performance
4. Press ** Play** to run

> **Note on Emulator Performance:** During development, the Android 
> emulator (API 37) failed to connect within the timeout period on the 
> development machine. This is a known issue on Windows machines without 
> sufficient RAM or where Intel HAXM hardware acceleration is not fully 
> configured. As a result, a **physical Android device was used** for all 
> testing and demonstration. The app was tested and confirmed working on 
> a real Android phone connected via USB debugging. Using a physical 
> device is actually recommended for more accurate real-world testing.

---
