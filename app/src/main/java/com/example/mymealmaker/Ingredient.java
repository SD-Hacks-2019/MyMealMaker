package com.example.mymealmaker;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.Parent;

import java.nio.ByteBuffer;
import java.util.List;


public class Ingredient implements Runnable{
    private Label fLabel;
    private byte[] fImage;
    private static AmazonRekognitionClient fMyClient;

    static final float MIN_CONFIDENCE = 90;
    static final String FOOD_LABEL = "Food";

    long id;

    public long getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public void run() {
        Image img = new Image();
        img.setBytes(ByteBuffer.wrap(fImage));
        DetectLabelsRequest req = new DetectLabelsRequest().withImage(img).withMinConfidence(MIN_CONFIDENCE);
        DetectLabelsResult res = fMyClient.detectLabels(req);
        List<Label> labels = res.getLabels();
        int maxNumParents = 0;
        Label maxNumParentsLabel = null;
        for(Label label : labels) {
            boolean isFood = false;
            for(Parent parent : label.getParents()) {
                if(parent.getName().equals(FOOD_LABEL)) {
                    isFood = true;
                }
                if(label.getName().equals("Vegetable") || label.getName().equals("Meat") || label.getName().equals("Produce")) {
                    isFood = false;
                }
            }
            if(label.getParents().size() > maxNumParents && isFood) {
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

    public boolean failedToFindFood() {
        return fLabel == null;
    }

    public byte[] getImageData() {
        return fImage;
    }
}
