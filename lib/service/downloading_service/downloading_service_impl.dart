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

  @override
  Future<void> stopDownload() {
    return _platform.invokeMethod('stopDownload');
  }
  // int testValue = 0;

  // try {
  //   final int result = await _platform.invokeMethod('testRequest');
  //   testValue = result;
  // } on PlatformException catch (_) {
  //   throw 'Exception';
  // }

  // return testValue;

}
