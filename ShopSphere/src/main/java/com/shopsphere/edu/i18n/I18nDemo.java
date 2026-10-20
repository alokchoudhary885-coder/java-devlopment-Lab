package com.shopsphere.edu.i18n;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Academic Demonstration: Java Internationalization (i18n) and Localization (L10n).
 * <p>
 * RTU Advanced Java Syllabus Traceability:
 * - Locale representation (Language, Country)
 * - Property Resource Bundles (messages_en.properties, messages_hi.properties)
 * - MessageFormat parameterized placeholders ({0})
 * - NumberFormat & Currency localization for monetary transactions
 * - DateTimeFormatter localization
 * </p>
 */
public class I18nDemo {

    public static void displayLocalizedStorefront(Locale locale) {
        System.out.println("======================================================================");
        System.out.println(" Internationalization (i18n) Demonstration: " + locale.getDisplayName());
        System.out.println(" Language: " + locale.getLanguage() + " | Country: " + locale.getCountry());
        System.out.println("======================================================================");

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

            // 1. Static Localized Strings
            System.out.println("[App Title]    : " + bundle.getString("app.title"));
            System.out.println("[App Tagline]  : " + bundle.getString("app.tagline"));
            System.out.println("[Nav - Cart]   : " + bundle.getString("nav.cart"));
            System.out.println("[Nav - Login]  : " + bundle.getString("nav.login"));
            System.out.println("[Button]       : " + bundle.getString("product.button.addToCart"));

            // 2. Parameterized MessageFormat ({0} discount and units)
            String rawDiscountPattern = bundle.getString("product.discount");
            String formattedDiscount = MessageFormat.format(rawDiscountPattern, 25);
            System.out.println("[Discount Msg] : " + formattedDiscount);

            String rawStockPattern = bundle.getString("product.stock.units");
            String formattedStock = MessageFormat.format(rawStockPattern, 48);
            System.out.println("[Stock Count]  : " + formattedStock);

            // 3. Localized Currency Formatting
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            BigDecimal samplePrice = new BigDecimal("14999.50");
            System.out.println("[Formatted Price] : " + currencyFormatter.format(samplePrice));

            // 4. Localized Date / Time
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(locale);
            System.out.println("[Order Date/Time] : " + dtFormatter.format(LocalDateTime.now()));

            System.out.println();
        } catch (Exception e) {
            System.err.println("Error loading resource bundle for locale " + locale + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 1. English (US)
        Locale enLocale = Locale.US;
        displayLocalizedStorefront(enLocale);

        // 2. Hindi (India)
        Locale hiLocale = Locale.of("hi", "IN");
        displayLocalizedStorefront(hiLocale);
    }
}
