
class AppInfo {
  String? name;
  String? packageName;
  String? versionName;
  int? versionCode;

  AppInfo(
    this.name,
    this.packageName,
    this.versionName,
    this.versionCode,
  );

  factory AppInfo.create(dynamic data) {
    return AppInfo(
      data["name"],
      data["package_name"],
      data["version_name"],
      data["version_code"],
    );
  }

  String getVersionInfo() {
    return "$versionName ($versionCode)";
  }
}
