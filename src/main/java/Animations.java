public class Animations {
    public static void addingSongs() throws InterruptedException {
        int numDots = 3;
        int repetitions = 3;
        int delay = 100;

        for (;;) {
            System.out.print("Adding songs");
            for (int j = 0; j < numDots; j++) {
                System.out.print(".");
                Thread.sleep(delay);
            }
            System.out.print("\r");
        }
    }

    public static void deletingSongs() throws InterruptedException {
        int numDots = 3;
        int repetitions = 3;
        int delay = 100;

        for (;;) {
            System.out.print("Deleting songs");
            for (int j = 0; j < numDots; j++) {
                System.out.print(".");
                Thread.sleep(delay);
            }
            System.out.print("\r");
        }
    }

    public static void finishAnimation() {
        String loadingString = "Finishing Work ";

        long startTime = System.currentTimeMillis();
        int index = 0;

        while (System.currentTimeMillis() - startTime < 3000) {
            String animatedLoadingString = "\u001B[36m" + loadingString.substring(index) + loadingString.substring(0, index);
            System.out.print("\r" + animatedLoadingString);
            index = (index + 1) % loadingString.length();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.err.println("Failed loading: " + e.getMessage());
            }
        }

        System.out.print("\r" + "\u001B[33mFinished! Thanks for using \u001B[32mSpotyClone!\n");
    }
}
