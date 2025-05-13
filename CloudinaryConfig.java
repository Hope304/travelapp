package com.example.travelapp;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dgig6a8uo");
            config.put("api_key", "539953685816862");
            config.put("api_secret", "oUZumSbmJoLpDlvIiqw8Vb755iY");
            config.put("secure", "true");
            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
}