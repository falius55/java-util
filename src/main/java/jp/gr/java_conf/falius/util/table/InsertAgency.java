package jp.gr.java_conf.falius.util.table;

import java.util.List;
import java.util.Map;

/**
 * データ挿入を請け負うクラス
 * @since 1.2.0
 */
class InsertAgency {
    private List<String> mColumnTitles;
    private Map<String, Object> mRowData;

    /**
     * @param columnTitles 列名のリスト
     * @param rowData          追加行の各データのマップ
     */
    InsertAgency(List<String> columnTitles, Map<String, Object> rowData) {
        mColumnTitles = columnTitles;
        mRowData = rowData;
    }

    /**
     * 指定された列に、データを挿入します。nullが渡された場合は、その列のデータは空であるものとします
     *
     * @param column 挿入する列の名前
     * @param data   挿入するデータ。nullが渡されるとデータを空にする
     * @return このオブジェクトの参照
     * @throws IllegalArgumentException インスタンス作成時に設定した列名以外の名前の列に挿入しようとした場合
     */
    public InsertAgency add(String column, Object data) {
        if (!mColumnTitles.contains(column)) {
            throw new IllegalArgumentException(
                    String.format("列名'%s'はこの表に設定されていません", column));
        }
        mRowData.put(column, data);
        return this;
    }

    /**
     * 指定されたインデックスの列に、データを挿入します
     *
     * @param columnIndex 挿入する列のインデックス
     * @param data        挿入するデータ。nullが渡されるとデータを空にする
     * @return このオブジェクトの参照
     * @throws IndexOutOfBoundsException 指定されたインデックスの列が設定されていない場合
     */
    public InsertAgency add(int columnIndex, Object data) {
        return add(mColumnTitles.get(columnIndex), data);
    }
}
