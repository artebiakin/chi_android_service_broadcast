part of '../home_page.dart';

class _DefaultView extends StatelessWidget {
  const _DefaultView(
    this.onStart, {
    Key? key,
  }) : super(key: key);

  final VoidCallback onStart;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisSize: MainAxisSize.max,
      children: [
        const Text('The file is ready to download: file.zip'),
        const SizedBox(height: 20),
        ElevatedButton(
          onPressed: onStart,
          child: const Text('Start Download'),
        ),
      ],
    );
  }
}
