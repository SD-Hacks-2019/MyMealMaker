package com.example.mymealmaker;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import java.nio.ByteBuffer;
import java.util.List;


public class Ingredient implements Runnable {
    private Label fLabel;
    private byte[] fImage;
    private static AmazonRekognitionClient fMyClient;

    public void run() {
        Image img = new Image();
        img.setBytes(ByteBuffer.wrap(fImage));
        DetectLabelsRequest req = new DetectLabelsRequest().withImage(img);
        DetectLabelsResult res = fMyClient.detectLabels(req);
        List<Label> labels = res.getLabels();
        int maxNumParents = 0;
        Label maxNumParentsLabel = null;
        for(Label label : labels) {
            if(label.getParents().size() > maxNumParents) {
                maxNumParents = label.getParents().size();
                maxNumParentsLabel = label;
            }
        }

        fLabel = maxNumParentsLabel;
    }

    Ingredient(String keyID, String secret, byte[] imageData) {
        fMyClient = new AmazonRekognitionClient(new BasicAWSCredentials(keyID, secret));
        fImage = imageData;
    }

    public Label getLabel() {
        return fLabel;
    }

    public byte[] getImageData() {
        return fImage;
    }
}
