class DownloadStatus {
  const DownloadStatus({
    required this.loadingPercentage,
    required this.loadingStatus,
  });

  final int loadingPercentage;
  final LoadingStatus loadingStatus;
}

enum LoadingStatus {
  ready,
  success,
  failed,
  paused,
  processing,
}
