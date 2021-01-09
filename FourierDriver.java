import java.util.Scanner;

public class FourierDriver {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        FourierRunner fileReader = null;
        String filename = "";

        int userInput;
        do {
            System.out.print("\n"); //creates space

            //displays the current file attached
            if(fileReader != null){
                System.out.println("Current file attached: " + filename);
            } else {
                System.out.println("No file currently attached");
            }

            userInput = userInput(in);
            switch(userInput){
                case 0:
                    System.out.println("Exiting program");
                    break;
                case 1:
                    if(fileReader != null){
                        fileReader.closeFile(); //closes the current file before opening a new one
                    }
                    filename = setFileAttaged(in);
                    fileReader = new FourierRunner(filename);
                    System.out.println(fileReader.getLengthOfFile());
                    break;
                case 2:
                    if(fileReader != null){
                    flattenFile(fileReader, in);
                    } else {
                        returnNoFileError(); //returns errorStatement if case 2 is called before a song file is attached
                    }
                    break;
                case 3:
                    createMonoFrequencyFile(in);
                    break;
                case 4:
                    createDualFrequencyFile(in);
                    break;
                case 5:
                    if(fileReader != null){
                        fileReader.createSongByAddingAllChannels(getFileNameFromUser(in));
                    } else {
                        returnNoFileError(); //returns errorStatement if case 2 is called before a song file is attached
                    }
                    break;
                case 6:
                    if(fileReader != null){
                        fileReader.createIdenticalSong(getFileNameFromUser(in));
                    } else {
                        returnNoFileError(); //returns errorStatement if case 2 is called before a song file is attached
                    }
                    break;
                default:
                    System.out.println("did not understand input, please try again");
                    break;    
            }
        } while(userInput != 0);

        if(fileReader != null){
            fileReader.closeFile(); //closes a file if one is open
        }
        in.close(); //closes scanner
    }

    /**
     * method allows user to create a mono channel song
     * @param in - a scanner to take user input
     */
    private static void createMonoFrequencyFile(Scanner in){
        String newFileName = getFileNameFromUser(in);
        System.out.println("What is the frequency?");
        double frequency = Double.parseDouble(in.nextLine());
        System.out.println("How long is the file (in seconds)?");
        double songLength = Double.parseDouble(in.nextLine());

        FourierRunner.monoFrequency(newFileName, songLength, frequency);
    }

    /**
     * method allows user to create a dual channel song
     * @param in- a scanner to take user input
     */
    private static void createDualFrequencyFile(Scanner in){
        String newFileName = getFileNameFromUser(in);
        System.out.println("What is the first frequency?");
        double firstFrequency = Double.parseDouble(in.nextLine());
        System.out.println("What is the second frequency?");
        double secondFrequency = Double.parseDouble(in.nextLine());
        System.out.println("How long is the file (in seconds)?");
        double songLength = Double.parseDouble(in.nextLine());

        FourierRunner.dualFrequency(newFileName, songLength, firstFrequency, secondFrequency);
    } 

    /**
     * method allows user to flatten a song with multiple channels into one channel
     * @param fileReader - the fourier runner holding the information about the initial file
     * @param in - a scanner to take user input
     */
    private static void flattenFile(FourierRunner fileReader, Scanner in){
        fileReader.createFlattenedFile(getFileNameFromUser(in));
        fileReader.closeFile();
    }

    /**
     * method gathers user input for what they want to do with the file
     * @param in - a scanner
     * @return the user choice
     */
    private static int userInput(Scanner in){
        System.out.println("please pick an option:\nEnter 0 to exit program\nEnter 1 to attach a file\n" +
                            "Enter 2 to create a flattened file\nEnter 3 to create a mono frequency file\n" +
                            "Enter 4 to create a dual frequency file\n" +
                            "Enter 5 to add the channels in order\nEnter 6 to create an identical song");
        return Integer.parseInt(in.nextLine());
    }

    /**
     * method gathers the filename for attaching a file from the user
     * @param in - a scanner
     * @return the user choice
     */
    private static String setFileAttaged(Scanner in){
        System.out.println("please enter the name of the file that you wish to attach (without the extension).");
        String fileName = in.nextLine();
        return fileName + ".wav";
    }

    /**
     * method gathers the a new filename from the user
     * @param in - a scanner
     * @return the user choice
     */
    private static String getFileNameFromUser(Scanner in){
        System.out.println("please enter the name of the file you wish to create (without the extension).");
        String fileName = in.nextLine();
        return fileName + ".wav";
    }

    /**
     * method prints out an error message if the user has not attached a file
     */
    private static void returnNoFileError(){
        System.out.println("you need to attach a file to the program first!");
    }
}