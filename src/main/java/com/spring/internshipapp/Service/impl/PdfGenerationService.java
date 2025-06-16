package com.spring.internshipapp.Service.impl;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.spring.internshipapp.Model.Student;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public static final String FONT_PATH = "fonts/arial.ttf";

    public byte[] generateInternshipConfirmationPdf(Student student) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont cyrillicFont = PdfFontFactory.createFont(FONT_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        Paragraph title = new Paragraph("ПОТВРДА ЗА ЗАВРШЕНА СТУДЕНТСКА ПРАКСА")
                .setFont(cyrillicFont)
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(title);

        String studentFullName = student.getName() + " " + student.getSurname();
        String studentIndex = student.getIndex() != null ? student.getIndex().toString() : "N/A";
        String internshipPosition = student.getInternship() != null ? student.getInternship().getPosition() : "N/A";
        String companyName = student.getInternship() != null && student.getInternship().getCompany() != null ?
                student.getInternship().getCompany().getName() : "N/A";

        Paragraph content = new Paragraph(
                String.format("Студентот %s со број на индекс %s успешно ја заврши својата студентска пракса како %s во компанијата %s.",
                        studentFullName, studentIndex, internshipPosition, companyName))
                .setFont(cyrillicFont)
                .setFontSize(12)
                .setMarginBottom(20);
        document.add(content);

        Paragraph additionalInfo = new Paragraph(
                String.format("Праксата е реализирана во согласност со правилата и прописите на факултетот и компанијата."))
                .setFont(cyrillicFont)
                .setFontSize(12)
                .setMarginBottom(40);
        document.add(additionalInfo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = LocalDate.now().format(formatter);

        Paragraph dateParagraph = new Paragraph("Датум: " + currentDate)
                .setFont(cyrillicFont)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(12)
                .setMarginBottom(60);
        document.add(dateParagraph);

        Paragraph signatureCoordinator = new Paragraph("___________________________\nКоординатор на пракса")
                .setFont(cyrillicFont)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(12);
        document.add(signatureCoordinator);

        document.close();
        return baos.toByteArray();
    }
}
