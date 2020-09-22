package com.example.demoApp.demo.Controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QRCodeController {

//    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
//        ByteArrayOutputStream stream = QRCode
//                .from(barcodeText)
//                .withSize(250, 250)
//                .stream();
//        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
//
//        return ImageIO.read(bis);
//    }
    @GetMapping(value = "/getBarCode/{qrCodeData}")
    public ResponseEntity<BufferedImage> createQRCode(@PathVariable String qrCodeData)
            throws WriterException, IOException {
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, 300, 300, hintMap);
//        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
//                .lastIndexOf('.') + 1), new File(filePath));
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        return ResponseEntity.ok(image);
    }
    @PostMapping(value = "/readQRCode")
    public static String readQRCode(@RequestBody BufferedImage bufferedImage)
            throws FileNotFoundException, IOException, NotFoundException {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        bufferedImage)));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }

    public static void main(String[] s) throws WriterException, IOException, NotFoundException{
        String qrCodeData = "9106729587";
        String filePath = "QRCode.png";
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BufferedImage image = new QRCodeController().createQRCode(qrCodeData).getBody();
        System.out.println(image);
        System.out.println(readQRCode(image));
    }
}
