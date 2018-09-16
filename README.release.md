Release Document
---

Enter the following command and choose the release version carefully

```
./gradlew assembleRelease -PversionCode=$VERSION_INT -PversionName=$VERSION_STRING
# e.g. ./gradlew assembleRelease -PversionCode=10001 -PversionName="1.0.1-beta"
```