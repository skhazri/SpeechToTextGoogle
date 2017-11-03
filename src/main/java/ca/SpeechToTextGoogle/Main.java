package ca.SpeechToTextGoogle;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import io.grpc.Context.Storage;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	public static void main(String... args) throws Exception {
		// Instantiates a client
		SpeechClient speech = SpeechClient.create();

		// The path to the audio file to transcribe
		String fileName = "./resources/audio.flac";

		// Reads the audio file into memory
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		ByteString audioBytes = ByteString.copyFrom(data);

		// Builds the sync recognize request
		RecognitionConfig config = RecognitionConfig.newBuilder().
				setEncoding(AudioEncoding.FLAC)
//				.setSampleRateHertz(16000)
				.setLanguageCode("en-US").build();
		RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

		// Performs speech recognition on the audio file
		RecognizeResponse response = speech.recognize(config, audio);
		List<SpeechRecognitionResult> results = response.getResultsList();

		for (SpeechRecognitionResult result : results) {
			System.out.println(result.getAlternativesCount());
			SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
			System.out.printf("Transcription: %s%n", alternative.getTranscript());
		}
		speech.close();
	}
}
