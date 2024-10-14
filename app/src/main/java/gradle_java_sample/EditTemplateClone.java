package gradle_java_sample;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 既存のExcelブックをもとに、コピーブックを作成・編集する処理サービス
 */
public class EditTemplateClone implements Runnable {
    /**
     * 新たに作成したブックを保存するディレクトリ
     */
    public static final String EXPORT_DIR_PATH = "./excel/";

    /**
     * テンプレートブックのパス
     */
    private final String templatePath;

    /**
     * 新たに作成するブックの名前
     */
    private final String newName;

    /**
     * log
     */
    private static final Logger log = LogManager.getLogger(EditTemplateClone.class);

    /**
     * テンプレートにするブックを渡す
     *
     * @param template 編集対象のブック
     * @param newName  新しく作成するブックの名称
     */
    public EditTemplateClone(final String templatePath, final String newName) {
        this.templatePath = templatePath;
        this.newName = newName;
    }

    @Override
    public void run() {
        try (final FileInputStream templateFileInputStream = new FileInputStream(templatePath)) {
            // テンプレートファイルから新しいブックを作成
            final XSSFWorkbook cloned = new XSSFWorkbook(templateFileInputStream);

            // 編集データの用意
            final List<List<String>> data = Arrays.asList(
                    Arrays.asList("hello", "world"),
                    Arrays.asList(getClass().getSimpleName(), "executed", "this", "program")
            );

            // データの書き込み
            // 先頭行はヘッダとして残す
            write(cloned, "sheet1", 1, 0, data);

            // 出力先ストリームを取得
            try (final FileOutputStream fileOutputStream = new FileOutputStream(getClonedFilePath())) {
                // 編集内容を保存
                cloned.write(fileOutputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * クローンされたファイルのパスを返す
     *
     * @return クローンされたファイルのパス
     */
    private String getClonedFilePath() {
        return EXPORT_DIR_PATH + newName;
    }

    /**
     * 指定のシートにデータを書き込む
     *
     * @param sheetName 編集するシートの名前
     * @param startRow  編集を開始する行(0から)
     * @param startCol  編集を開始する列(0から)
     * @param data      入力するデータ
     */
    private void write(final Workbook target, final String sheetName, int startRow, int startCol,
                       final List<List<String>> data) {
        // 指定名称のシートを取得
        final Sheet sheet = target.getSheet(sheetName);
        if (Objects.isNull(sheet)) {
            // 指定のシート名が存在しなかった
            throw new RuntimeException("the sheet name :" + sheetName + " is not exists in book");
        }

        for (int i = 0; i < data.size(); i++) {
            final int currentRow = startRow + i;
            Row row = sheet.getRow(currentRow);
            // if row(i) is not exists, create new row
            if (row == null) row = sheet.createRow(currentRow);
            for (int j = 0; j < data.get(i).size(); j++) {
                final int currentCol = startCol + j;
                final Cell cell = row.createCell(currentCol);
                final String cellValue = data.get(i).get(j);
                log.info("{} : {} = {}", currentRow, currentCol, cellValue);
                System.out.println(currentRow + " : " + currentCol + " = " + cellValue);
                cell.setCellValue(cellValue);
            }
        }
    }
}
