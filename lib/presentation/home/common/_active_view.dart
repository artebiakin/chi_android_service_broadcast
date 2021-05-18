part of '../home_page.dart';

class _ActiveView extends StatelessWidget {
  const _ActiveView({
    Key? key,
    required this.loadingPercentage,
    required this.status,
    required this.onStop,
  }) : super(key: key);

  final VoidCallback onStop;
  final int loadingPercentage;
  final String status;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 35),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 15),
            child: Text(
              '$loadingPercentage%',
              style: Theme.of(context).textTheme.headline1,
            ),
          ),
          LinearProgressIndicator(
            value: loadingPercentage / 100,
          ),
          const SizedBox(height: 7),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Status:'),
              Text(status),
            ],
          ),
          const SizedBox(height: 20),
          Center(
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                primary: Colors.black54,
              ),
              onPressed: onStop,
              child: const Text('Stop'),
            ),
          ),
        ],
      ),
    );
  }
}
