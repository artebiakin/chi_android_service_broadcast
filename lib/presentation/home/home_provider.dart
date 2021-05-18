import 'dart:async';

import 'package:flutter/material.dart';

import '../../injection_container.dart';
import '../../service/downloading_service/downloading_service.dart';
import '../../service/downloading_service/model/download_status.dart';

class HomeProvider extends ChangeNotifier {
  int loadingPercentage = 0;
  LoadingStatus _status = LoadingStatus.ready;

  LoadingStatus get status => _status;

  set status(LoadingStatus status) {
    if (_status != status) {
      _status = status;
      notifyListeners();
    }
  }

// Draft
  late StreamSubscription<int> steam;

  String get textStatus {
    switch (_status) {
      case LoadingStatus.processing:
        return 'In progress';
      case LoadingStatus.failed:
        return 'Failed';
      case LoadingStatus.paused:
        return 'Pause';
      default:
        return '';
    }
  }

  bool get isPause => status == LoadingStatus.paused ? true : false;

  final DownloadingService downloadingService = sl<DownloadingService>();

  void onStart() {
    status = LoadingStatus.processing;
    // downloadingService.startDownload();

    // steam = downloadingService.testRequest().listen((event) {
    //   if (event == 100) onStop();

    //   loadingPercentage = event;
    //   notifyListeners();
    // });
  }

  void onStop() {
    status = LoadingStatus.ready;
    // loadingPercentage = 0;

    // steam.cancel();
    // downloadingService.stopDownload();
  }

  void onPause() {
    status = LoadingStatus.paused;
  }

  void onResume() {
    status = LoadingStatus.processing;
  }
}
