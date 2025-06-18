package com.spring.internshipapp.Service.impl;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.spring.internshipapp.Model.JournalEntry;
import com.spring.internshipapp.Model.Student;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public static final String FONT_PATH = "fonts/arial.ttf";
    public static final String FONT_BOLD_PATH = "fonts/arialbd.ttf";

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

    public byte[] generateStudentJournalPdf(Student student, JournalEntry journalEntry) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        PdfFont cyrillicFont = PdfFontFactory.createFont(FONT_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        PdfFont boldCyrillicFont = PdfFontFactory.createFont(FONT_BOLD_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED); // Load bold if separate, or rely on .setBold()

        document.add(new Paragraph("Дневник за Студентска Пракса")
                .setFont(boldCyrillicFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        String studentInfo = String.format("Студент: %s %s (Индекс: %s)",
                student.getName(), student.getSurname(), student.getIndex() != null ? student.getIndex().toString() : "N/A");
        document.add(new Paragraph(studentInfo)
                .setFont(cyrillicFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(25));

        if (journalEntry != null && journalEntry.getContent() != null && !journalEntry.getContent().isEmpty()) {
            String journalContent = journalEntry.getContent();
            String[] weeklyEntries = journalContent.split("(?=Недела\\s\\d+:)");

            for (String entryPart : weeklyEntries) {
                entryPart = entryPart.trim();
                if (entryPart.isEmpty()) continue;

                if (entryPart.startsWith("Недела")) {
                    int colonIndex = entryPart.indexOf(':');
                    if (colonIndex != -1) {
                        String weekHeader = entryPart.substring(0, colonIndex + 1);
                        String weekContent = entryPart.substring(colonIndex + 1).trim();

                        document.add(new Paragraph(weekHeader)
                                .setFont(boldCyrillicFont)
                                .setFontSize(12)
                                .setMarginTop(10)
                                .setMarginBottom(5));
                        if (!weekContent.isEmpty()) {
                            document.add(new Paragraph(weekContent)
                                    .setFont(cyrillicFont)
                                    .setFontSize(11)
                                    .setMultipliedLeading(1.2f)
                                    .setMarginBottom(15));
                        }
                    } else {
                        document.add(new Paragraph(entryPart).setFont(cyrillicFont).setFontSize(11).setMarginBottom(15));
                    }
                } else {
                    document.add(new Paragraph(entryPart).setFont(cyrillicFont).setFontSize(11).setMarginBottom(15));
                }
            }
        } else {
            document.add(new Paragraph("Студентот нема внесено содржина во дневникот.")
                    .setFont(cyrillicFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        document.close();
        return baos.toByteArray();
    }
}
