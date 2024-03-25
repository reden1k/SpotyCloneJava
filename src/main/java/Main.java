public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\u001B[32mWelcome to SpotyClone!\u001B[0m");
        Thread.sleep(5000);
        User.createConfig();
        ClonerSongs.clonePlaylist();
        User.deleteTemporaryFiles();
        Animations.finishAnimation();

    }
}
