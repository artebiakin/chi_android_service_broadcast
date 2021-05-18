import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../service/downloading_service/model/download_status.dart';
import 'home_provider.dart';

part 'common/_active_view.dart';
part 'common/_default_view.dart';

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => HomeProvider(),
      builder: (context, child) {
        final provider = Provider.of<HomeProvider>(context);

        final status = provider.status;

        return Scaffold(
          body: Center(
            child: AnimatedSwitcher(
              duration: const Duration(milliseconds: 350),
              child: status == LoadingStatus.ready
                  ? _DefaultView(provider.onStart)
                  : _ActiveView(
                      status: provider.textStatus,
                      loadingPercentage: provider.loadingPercentage,
                    ),
            ),
          ),
        );
      },
    );
  }
}
