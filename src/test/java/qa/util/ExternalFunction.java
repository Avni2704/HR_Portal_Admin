package qa.util;

import drivers.DriverInstance;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.fail;

public class ExternalFunction extends DriverInstance {

    private static final String DOWNLOAD_DIR = System.getProperty("user.home") + "/Downloads";

    public static void waitForLoaderToDisappear(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(120)).until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("screen-loading")
        ));
    }

    public static void waitForTableToLoad(WebDriver driver, String tableCssSelector, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        // Wait for table element to appear
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(tableCssSelector)));

        // Wait until table has at least one row
        wait.until(driver1 -> {
            try {
                return !table.findElements(By.cssSelector("tbody tr")).isEmpty();
            } catch (StaleElementReferenceException e) {
                return false; // recheck if DOM updated
            }
        });

        System.out.println("Table loaded and contains rows.");
    }

    public static void cleanUpOldDownloads(String prefix) {
        File downloadDir = new File(DOWNLOAD_DIR);
        File[] oldFiles = downloadDir.listFiles((dir, name) -> name.startsWith(prefix));

        if (oldFiles != null) {
            for (File f : oldFiles) {
                boolean deleted = f.delete();
                if (deleted) {
                    System.out.println("🧹 Deleted old file: " + f.getName());
                }
            }
        }
    }

    /**
     * Optionally check if a file with given prefix exists in Downloads.
     */
    public static boolean isFileDownloaded(String prefix) {
        File downloadDir = new File(DOWNLOAD_DIR);
        File[] files = downloadDir.listFiles((dir, name) ->
                name.startsWith(prefix) && name.endsWith(".xlsx"));

        return files != null && files.length > 0;
    }

    public static void verifyColumnHasValidData(WebDriver driver, int columnIndex, String columnName) {
        List<WebElement> cells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + columnIndex + ")"));

        List<String> values = cells.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());

        System.out.println("=== Verifying Column: " + columnName + " ===");
        System.out.println("Extracted " + values.size() + " rows.");

        boolean allValid = true;
        int emptyCount = 0;

        for (int i = 0; i < values.size(); i++) {
            String val = values.get(i);
            System.out.println("Row " + (i + 1) + ": [" + val + "]");

            // Basic validation: empty
            if (val.isEmpty() && !columnName.equalsIgnoreCase("Remark")) {
                System.out.println("Invalid data at row " + (i + 1) + ": " + val);
                allValid = false;
                emptyCount++;
            }
        }

        System.out.println("=== Verification Result for " + columnName + " ===");
        if (allValid) {
            System.out.println("All " + columnName + " values are valid and non-empty.");
        } else {
            System.out.println("Found " + emptyCount + " empty entries in " + columnName + " column.");
            fail("Column [" + columnName + "] contains empty values: " + values);
        }
    }

    public static int getColumnIndexByHeader(WebDriver driver, String headerName) {
        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(headerName)) {
                return i + 1; // nth-child starts from 1
            }
        }
        fail("Column header not found: " + headerName);
        return -1;
    }

    // Get column index dynamically by header name
    public static int getColumnIndexByName(WebDriver driver, String columnName) {
        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                return i + 1; // nth-child starts from 1
            }
        }
        throw new NoSuchElementException("No header found for column: " + columnName);
    }

    public static boolean isDateOrTime(String value) {
        String[] dateTimePatterns = {
                "dd MMM yyyy, HH:mm", "dd MMM yyyy HH:mm", "dd/MM/yyyy HH:mm",
                "yyyy-MM-dd HH:mm", "MM/dd/yyyy HH:mm",
                "dd MMM yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy",
                "hh:mm a", "HH:mm" // time-only formats
        };
        for (String pattern : dateTimePatterns) {
            try {
                new SimpleDateFormat(pattern, Locale.ENGLISH).parse(value);
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // Parse date or time safely
    public static Date parseDateOrTime(String value) {
        String[] dateTimePatterns = {
                "dd MMM yyyy, HH:mm", "dd MMM yyyy HH:mm", "dd/MM/yyyy HH:mm",
                "yyyy-MM-dd HH:mm", "MM/dd/yyyy HH:mm",
                "dd MMM yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy",
                "hh:mm a", "HH:mm"
        };
        for (String pattern : dateTimePatterns) {
            try {
                return new SimpleDateFormat(pattern, Locale.ENGLISH).parse(value);
            } catch (Exception ignored) {}
        }
        return null;
    }

    // Detect if string is numeric
    public static boolean isNumeric(String value) {
        return value.matches("-?\\d+(\\.\\d+)?");
    }

    // Extract and normalize column values
    public static List<Object> extractColumnValues(WebDriver driver, String columnName) {
        int columnIndex = getColumnIndexByName(driver, columnName);
        List<String> cellValues = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + columnIndex + ")"))
                .stream()
                .map(e -> e.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());

        List<Object> normalized = new ArrayList<>();
        for (String value : cellValues) {
            if (isDateOrTime(value)) {
                normalized.add(parseDateOrTime(value));
                System.out.println("Detected Date/Time → " + value);
            } else if (isNumeric(value)) {
                normalized.add(Double.parseDouble(value));
                System.out.println("Detected Number → " + value);
            } else {
                normalized.add(value.toLowerCase());
                System.out.println("Detected Text → " + value);
            }
        }
        return normalized;
    }

    // Sort ascending (handles text, date, number, time)
    public static List<Object> sortAscending(List<Object> values) {
        List<Object> sorted = new ArrayList<>(values);
        sorted.sort((a, b) -> {
            if (a instanceof Date && b instanceof Date)
                return ((Date) a).compareTo((Date) b);
            else if (a instanceof Double && b instanceof Double)
                return ((Double) a).compareTo((Double) b);
            else
                return a.toString().compareToIgnoreCase(b.toString());
        });
        return sorted;
    }

    // Sort descending (handles text, date, number, time)
    public static List<Object> sortDescending(List<Object> values) {
        List<Object> sorted = new ArrayList<>(values);
        sorted.sort((a, b) -> {
            if (a instanceof Date && b instanceof Date)
                return ((Date) b).compareTo((Date) a);
            else if (a instanceof Double && b instanceof Double)
                return ((Double) b).compareTo((Double) a);
            else
                return b.toString().compareToIgnoreCase(a.toString());
        });
        return sorted;
    }

    // Click sorting icon for a given column
    public static void clickSortingIcon(WebDriver driver, String columnName) {
        waitForTableToLoad(driver, ".app-table", 15);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
            WebElement targetHeader = headers.stream()
                    .filter(h -> h.getText().trim().equalsIgnoreCase(columnName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No header found for column: " + columnName));

            WebElement sortIcon = targetHeader.findElement(By.cssSelector("img.table__sortby"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortIcon);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortIcon);

            System.out.println("Clicked sorting icon for column: " + columnName);
            waitForTableToLoad(driver, ".app-table", 15);

        } catch (Exception e) {
            System.out.println("Error clicking sorting icon for " + columnName + ": " + e.getMessage());
        }
    }

    public static WebElement findElementInPaginatedTable(WebDriver driver, WebDriverWait wait,
                                                         By cellLocator, String textToFind, int maxPages) {

        By nextPageButton = By.cssSelector("button.app-table__arrow--next[aria-label='next']");
        boolean found = false;
        WebElement targetElement = null;
        int currentPage = 0;

        while (!found && currentPage < maxPages) {
            currentPage++;

            // Check current page
            List<WebElement> elements = driver.findElements(cellLocator);
            for (WebElement e : elements) {
                if (e.getText().trim().equals(textToFind)) {
                    wait.until(ExpectedConditions.visibilityOf(e));
                    targetElement = e;
                    found = true;
                    break;
                }
            }

            if (!found) {
                List<WebElement> nextBtnList = driver.findElements(nextPageButton);
                if (!nextBtnList.isEmpty() && nextBtnList.get(0).isEnabled()) {

                    // Click Next
                    nextBtnList.get(0).click();

                    // Wait for table to reload
                    waitForTableToLoad(driver, ".app-table", 15);

                    // small sleep to let React/MUI render new page
                    try {
                        Thread.sleep(1000); // 1 second, adjust if needed
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    break; // no more pages
                }
            }
        }

        if (!found) {
            throw new RuntimeException("Element '" + textToFind + "' not found in table after " + maxPages + " pages");
        }

        return targetElement;
    }

    // Export file validation
    public static List<List<String>> getUITableData() {
        List<List<String>> tableData = new ArrayList<>();

        List<WebElement> rows = driver.findElements(
                By.cssSelector("table tbody tr")
        );

        for (WebElement row : rows) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> cells = row.findElements(By.cssSelector("td"));

            for (WebElement cell : cells) {
                rowData.add(cell.getText().trim());
            }
            tableData.add(rowData);
        }
        return tableData;
    }

    public static List<String> getVisibleTableHeaders() {
        List<String> headers = new ArrayList<>();

        List<WebElement> headerElements = driver.findElements(
                By.cssSelector("table thead th")
        );

        for (WebElement header : headerElements) {
            if (header.isDisplayed()) {
                headers.add(header.getText().trim());
            }
        }
        return headers;
    }

    public static List<List<String>> readExcelData(File file) throws Exception {
        List<List<String>> excelData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(cell.toString().trim());
                }
                excelData.add(rowData);
            }
        }
        return excelData;
    }

    public static File waitForExportedFile() throws InterruptedException {
        File dir = new File(System.getProperty("user.home") + "/Downloads");
        File latestFile = null;

        for (int i = 0; i < 10; i++) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".xlsx"));
            if (files != null && files.length > 0) {
                latestFile = Arrays.stream(files)
                        .max(Comparator.comparingLong(File::lastModified))
                        .orElse(null);
                break;
            }
            Thread.sleep(1000);
        }

        if (latestFile == null) {
            Assert.fail("Exported file was not found in Downloads folder");
        }
        return latestFile;
    }




}
