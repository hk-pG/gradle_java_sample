package gradle_java_sample;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 既存のExcelブックをもとに、コピーブックを作成・編集する処理サービス
 */
public class EditTemplate implements Runnable {
    /**
     * 新たに作成したブックを保存するディレクトリ
     */
    public static final String EXPORT_DIR_PATH = "./excel/";

    /**
     * テンプレートブック
     */
    private final Workbook template;

    private final String newName;

    /**
     * テンプレートにするブックを渡す
     *
     * @param template 編集対象のブック
     * @param newName  新しく作成するブックの名称
     */
    public EditTemplate(final Workbook template, final String newName) {
        this.template = template;
        this.newName = newName;
    }

    @Override
    public void run() {
        // 一番上の行は固定してあるので、2行目からデータを書き込む
        write("sheet1", 1, 1, new String[][]{
                new String[]{
                        "Hello World", "this is a test for POI"
                },
                new String[]{
                        getClass().getSimpleName()
                },
                new String[]{
                        "Good By"
                }
        });
        // 別名でブックを保存する
        saveAsNewName();
    }

    private void saveAsNewName() {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(EXPORT_DIR_PATH + newName)) {
            template.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 指定のシートにデータを書き込む
     *
     * @param sheetName 編集するシートの名前
     * @param startRow  編集を開始する行
     * @param startCol  編集を開始する列
     * @param data      入力するデータ
     */
    private void write(final String sheetName, int startRow, int startCol, final String[][] data) {
        // 指定名称のシートを取得
        final Sheet sheet = template.getSheet(sheetName);
        if (Objects.isNull(sheet)) {
            // 指定のシート名が存在しなかった
            throw new RuntimeException("the sheet name :" + sheetName + " is not exists in book");
        }

        final int maxRow = data.length;
        for (int currentRow = startRow; currentRow < maxRow; currentRow++) {
            final int maxCol = data[currentRow].length;
            final Row row = sheet.getRow(currentRow);
            for (int currentCol = startCol; currentCol < maxCol; currentCol++) {
                final Cell cell = row.getCell(currentCol);
                cell.setCellValue(data[currentRow][currentCol]);
            }
        }
    }

}
