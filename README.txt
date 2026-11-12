SECOND ALARM - Android Studio project

Purpose:
- One-time alarm for Android 8.0 and later.
- Date plus HH:MM:SS input.
- Uses AlarmManager.setExactAndAllowWhileIdle with RTC_WAKEUP.

Build:
1. Install Android Studio on a computer.
2. Extract this ZIP.
3. In Android Studio choose Open, then select the extracted SecondAlarm folder.
4. Allow Gradle sync to finish. Android Studio may download the Android Gradle plugin and SDK components.
5. Choose Build > Build Bundle(s) / APK(s) > Build APK(s).
6. Find the APK using the "locate" link in the build notification, then transfer it to the TECNO phone and install it.

Important limitations:
- This source project is not a prebuilt APK.
- Android/TECNO battery management can delay alarms. Exempt Second Alarm from battery optimization if that option is available.
- Keep the phone powered on. Alarms scheduled by this starter version are cleared by a reboot.
- The app is designed for a single pending alarm; setting a new one replaces the previous one.
- Test it with a time 1-2 minutes in the future before relying on it.
