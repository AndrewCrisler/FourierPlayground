import java.util.ArrayList;
import java.util.List;

import importedFiles.*;

public class FourierRunner {
    private static int numChannels;
    private static int validBits;
    private static long sampleRate;
    private static long numFrames;
    private static WavFile file;

    private static final List<List<Double>> channels = new ArrayList<List<Double>>();
    
    public FourierRunner(String filename){
        System.out.println(filename);
        file = new WavFile(filename);
        numChannels = file.getNumChannels();
        validBits = file.getValidBits();
        sampleRate = file.getSampleRate();
        numFrames = file.getNumFrames();

        seperateChannels(file.getSamples());
    }

    /**
     * method returns the length of the song in seconds
     * @return a decimal value of the length of the song
     */
    public double getLengthOfFile(){
        return numFrames/sampleRate/numChannels;
    }

    /**
     * Static method creates a song with one channel.
     * @param newFileName - the filename for the new song
     * @param songLength - the length of the song in seconds
     * @param frequency - the frequency of the song
     */
    public static void monoFrequency(String newFileName, double songLength, double frequency){

        final long newSampleRate = 8000; //preset value
        final int newNumChannels = 1; //preset value
        final int newValidBits = 8; //preset value

        final long newNumFrames = (long)(songLength*newSampleRate);

        WavFile soundFile = new WavFile(newFileName, newNumChannels, newNumFrames, newValidBits, newSampleRate);

        ArrayList<Double> samples = new ArrayList<>();

        for(int i = 0; i < newNumFrames; i++){
            samples.add(i, Math.sin(2 * Math.PI * i * ((double)frequency/newSampleRate)));
        }

        soundFile.setSamples(samples);
        soundFile.close();
    }

    /**
     * Static method creates a song with two channels. Each outputting a seperate frequency
     * @param newFileName - the filename for the new double frequency song
     * @param songLength - the length of the song
     * @param firstFrequency - the frequency of the first channel
     * @param secondFrequency - the frequency of the second channel
     */
    public static void dualFrequency(String newFileName, double songLength, double firstFrequency, double secondFrequency){

        final long newSampleRate = 8000; //preset value
        final int newNumChannels = 2; //preset value
        final int newValidBits = 8; //preset value

        final long newNumFrames = (long)(newSampleRate*songLength*newNumChannels);

        WavFile soundFile = new WavFile(newFileName, newNumChannels, newNumFrames, newValidBits, newSampleRate);

        ArrayList<Double> samples = new ArrayList<>();

        for(int i = 0; i < newNumFrames; i += 2){
            samples.add(i, Math.sin(2 * Math.PI * i/2 * (firstFrequency / newSampleRate)));
            samples.add((i + 1), Math.sin(2 * Math.PI * i/2 * (secondFrequency / newSampleRate)));
        }

        soundFile.setSamples(samples);
        soundFile.close();
    }

    /**
     * (currently for testing)
     * method creates a file that adds the all channels one after another
     * @param newFileName the new file name for the music file
     */
    public void createSongByAddingAllChannels(String newFileName){
        long newNumFrames = (long)channels.size() * numFrames;
        ArrayList<Double> tempArrayList = new ArrayList<>();
        for(int channel = 0; channel < numChannels; channel++){
            int index = tempArrayList.size();
            tempArrayList.addAll(index, channels.get(channel));
        }

        WavFile allChannelsFile = new WavFile(newFileName, 1, newNumFrames, validBits, sampleRate);
        allChannelsFile.setSamples(tempArrayList);
        allChannelsFile.close();
    }
    
    /**
     * method closes the file
     */
    public void closeFile(){
        file.close();
    }

    /**
     * (currently for testing)
     * method mixes all channels into one channel
     */
    public void createFlattenedFile(String newFileName){
        if(numChannels > 1){
            ArrayList<Double> flatValues = new ArrayList<>();
            for(int frame = 0; frame < channels.get(0).size(); frame++){
                double mixFrameTotal = 0;
                for(int channel = 0; channel < channels.size(); channel++){
                    mixFrameTotal += channels.get(channel).get(frame);
                }
                double flatValue = mixFrameTotal/numChannels;
                flatValues.add(flatValue);
            }
        
            WavFile flattenedFile = new WavFile(newFileName, 1, numFrames/numChannels, validBits, sampleRate);
            flattenedFile.setSamples(flatValues);
            flattenedFile.close();
        } else {
            System.out.println("song already has only one channel");
        }
    }

    /**
     * (currently for testing)
     * method creates an identical song to the song attached
     * @param newFileName - the new file name for the song
     */
    public void createIdenticalSong(String newFileName){
        WavFile identicalSong = new WavFile(newFileName, numChannels, numFrames, validBits, sampleRate);
        ArrayList<Double> samples = new ArrayList<Double>();
        for(int frame = 0; frame < numFrames/numChannels; frame++){
            for(int channel = 0; channel < numChannels; channel++){
                samples.add(channels.get(channel).get(frame));
            }
        }
        identicalSong.setSamples(samples);
        identicalSong.close();
    }

    /**
     * helper method seperates the channels into seperate arrays
     * @param allChannels the full array containing all channels
     */
    private void seperateChannels(ArrayList<Double> allChannels){
        System.out.println("length of samples: " + allChannels.size());
        if(numChannels == 1){
            channels.add(allChannels);
        } else {
            for(int channel = 0; channel < numChannels; channel++){
                ArrayList<Double> channelData = new ArrayList<>();
                for(int frame = channel; frame < allChannels.size(); frame+=numChannels){
                    channelData.add(allChannels.get(frame));
                }
                channels.add(channelData);
            }
        }
        

        //calculate the frames taken in (for testing)
        long totalSize = 0;
        for(int arr = 0; arr < numChannels; arr++){
            totalSize += channels.get(arr).size();
            System.out.println(channels.get(arr).size());
        }
        System.out.println("total size recorded: " + totalSize + " - total frames: " + numFrames);
    }
}