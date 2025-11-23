# ☀️ Modular Weather App

This is an example mobile application developed in Kotlin and Jetpack Compose, demonstrating the implementation of a Modular Clean Architecture on Android.

It allows users to get the current weather forecast based on the device's geographical location or by manually searching for cities.

## 🔑 API Key Requirement

You need an API key from:  
**https://openweathermap.org/current**

Then place it in your `local.properties` file: 

OPENWEATHER_API_KEY="INSERT_API_KEY_HERE"
---

## 🔑 Key Features

- **Local Weather:** Optional location fetching for local weather.  
- **City Search:** Real-time city search function with suggestions (Geocoding).  
- **Modern Design:** User interface built using Jetpack Compose and Material 3.  

---

## 🧱 Project Architecture

The project is divided into modules following the Clean Architecture principles (Domain / Data / Presentation) to ensure scalability, maintainability, and fast build times.

| Module | Responsibility |
|--------|----------------|
| **:app** | Assembly: Contains the MainActivity, global Hilt configuration (DI), and the navigation container (Compose Navigation Host). |
| **:core** | Domain: Contains interfaces (Repository Contracts) and Domain Models (WeatherModel, CityModel). It must not have dependencies on other project modules. |
| **:data** | Infrastructure: Implements Repositories and data source logic (Retrofit, OkHttp, Fused Location Provider Client). Contains DTOs and the DTO → Domain mapping logic. |
| **:feature:weather** | Presentation: Contains the logic for the Weather feature (ViewModels, Use Cases) and the User Interface (Compose). Includes Domain → UI mapping logic. |

---

## 🛠️ Technologies Used

- **Language:** Kotlin  
- **UI:** Jetpack Compose (Material 3, Navigation)  
- **Architecture:** Clean Architecture (Modular)  
- **Asynchrony:** Kotlin Coroutines & Flow  
- **Dependency Injection:** Hilt (Dagger)  
- **Networking:** Retrofit, OkHttp (with API Key interceptors)  
- **Serialization:** Gson  

---
<img width="108" height="240" alt="Screenshot_20251123_132212" src="https://github.com/user-attachments/assets/79af8f34-c08b-460e-b7d8-ac1dc10b40f4" />
<img width="108" height="240" alt="Screenshot_20251123_132224" src="https://github.com/user-attachments/assets/5c5b8992-03e4-4715-8dfc-454ca54e66f3" />
<img width="108" height="240" alt="Screenshot_20251123_132241" src="https://github.com/user-attachments/assets/ca554031-8b0e-44e4-890c-0ee706b5926f" />
<img width="108" height="240" alt="Screenshot_20251123_132454" src="https://github.com/user-attachments/assets/2214e351-0f55-448c-93a4-25d60fab3301" />
<img width="108" height="240" alt="Screenshot_20251123_132507" src="https://github.com/user-attachments/assets/59fef6ce-ae08-4fbf-889f-cb33dcfa3d1d" />
<img width="108" height="240" alt="Screenshot_20251123_132530" src="https://github.com/user-attachments/assets/13d56788-6495-4016-a0cb-7608b95bde57" />




