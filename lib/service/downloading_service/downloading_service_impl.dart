import 'package:flutter/services.dart';

import 'downloading_service.dart';

class DownloadingServiceImpl extends DownloadingService {
  static const _platform =
      MethodChannel('chi.android.service.broadcast/downloading');

  static const _stream = EventChannel('downloadStatusStream');

  @override
  Stream<int> testRequest() =>
      _stream.receiveBroadcastStream().map<int>((event) => event as int);

  @override
  Future<void> startDownload() {
    return _platform.invokeMethod('startDownload');
  }
}
