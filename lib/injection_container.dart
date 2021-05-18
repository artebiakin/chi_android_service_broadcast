import 'package:get_it/get_it.dart';

import 'service/downloading_service/downloading_service.dart';
import 'service/downloading_service/downloading_service_impl.dart';

final sl = GetIt.instance;

Future<void> init() async {
  sl

      // Service
      .registerLazySingleton<DownloadingService>(
          () => DownloadingServiceImpl());
}
