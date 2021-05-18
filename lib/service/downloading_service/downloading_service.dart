abstract class DownloadingService {
  Stream<int> testRequest();

  Future<void> startDownload();

  Future<void> stopDownload();
}
