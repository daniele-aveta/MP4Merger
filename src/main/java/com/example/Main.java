package com.example;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Examples:
 *  - https://www.tabnine.com/code/java/classes/org.mp4parser.muxer.container.mp4.MovieCreator
 *  - https://java.hotexamples.com/it/examples/com.googlecode.mp4parser.authoring.container.mp4/MovieCreator/build/java-moviecreator-build-method-examples.html
 *
 * Maven:
 *  - https://search.maven.org/artifact/com.googlecode.mp4parser/isoparser/1.1.22/jar
 */
public class Main {

    public static void main(String[] args) throws IOException {
        MovieCreator mc = new MovieCreator();
        Movie movie1 = mc.build("./test1.mp4");
        Movie movie2 = mc.build("./test2.mp4");

        //Fetching the video tracks from the movies and storing them into an array
        Track[] vetTrackVideo = new Track[0];
        vetTrackVideo = Stream.of(movie1, movie2)
                .flatMap(movie -> movie.getTracks().stream())
                .filter(movie -> movie.getHandler().equals("vide"))
                .collect(Collectors.toList())
                .toArray(vetTrackVideo);

        //Fetching the audio tracks from the movies and storing them into an array
        Track[] vetTrackAudio = new Track[0];
        vetTrackAudio = Stream.of(movie1, movie2)
                .flatMap(movie -> movie.getTracks().stream())
                .filter(movie -> movie.getHandler().equals("soun"))
                .collect(Collectors.toList())
                .toArray(vetTrackAudio);

        //Creating the output movie by setting a list with both video and audio tracks
        Movie movieOutput = new Movie();
        List<Track> listTracks = new ArrayList<>(List.of(new AppendTrack(vetTrackVideo), new AppendTrack(vetTrackAudio)));
        movieOutput.setTracks(listTracks);

        //Building the output movie and storing it into a Container
        DefaultMp4Builder mp4Builder = new DefaultMp4Builder();
        Container c = mp4Builder.build(movieOutput);

        //Writing the output file
        FileOutputStream fos = new FileOutputStream("output.mp4");
        c.writeContainer(fos.getChannel());
        fos.close();
    }
}