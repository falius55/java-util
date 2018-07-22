package jp.gr.java_conf.falius.util.regex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
     *  <p>正規表現にマッチした各部分文字列を扱う内部クラスです
     *  <p>Regexインスタンスからfind(int)に有効値を渡すことで取り出せます
     *  <p>Dataインスタンスのgroup(int)を使うことで扱っている部分文字列全体とグルーピングした文字列を取り出せます
     */
public class PartData implements Iterable<String> {
    private static final String[] EMPTY_STRING_ARRAY = new String[0]; // toArray()で使いまわす空の配列
    // data.get(0) : 扱う部分文字列全体
    // data.get(1) : 扱う部分文字列のうちグループ化された文字列のひとつ目
    private final List<String> mData;

    PartData(List<String> data) {
        mData = new ArrayList<String>(data);
    }

    /**
     *  マッチ箇所の文字列を返します
     *  group(0)と同義です
     *  @return マッチ箇所の文字列
     */
    public String group() {
        return group(0);
    }

    /**
     *  マッチした全体文字列及びグルーピング文字列を返します
     *  @param  index   取り出す文字列のインデックス。0ならマッチ箇所全体、1ならひとつ目のグルーピング文字列
     *  @return マッチした全体文字列及びグルーピング文字列。見つからなければnull
     *  @throws IndexOutOfBoundsException 引数に有効範囲外のインデックスが渡された場合
     */
    public String group(int index) {
        if (index < 0 || index > size() - 1) {
            throw new IndexOutOfBoundsException(
                    String.format("有効なインデックスは0から%dです : passed index=%d", size() - 1, index));
        }
        return mData.get(index);
    }

    /**
     *  扱うデータの数を返します
     *  全体文字列とグルーピングの数の合計なので、groupCount()+1と同義です
     *  @return 扱うデータの数
     */
    public int size() {
        return mData.size();
    }

    /**
     *  グルーピングの数を返します
     *  @return グルーピングの数
     */
    public int groupCount() {
        return mData.size() - 1;
    }

    /**
     *  保持しているデータを配列にして返します
     *  @return 扱うデータの配列。[0]にマッチ文字列全体が入り、[1]にひとつ目のグループを表す文字列が入る
     */
    public String[] toArray() {
        return mData.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     *  扱うデータの文字列表現を返します
     */
    @Override
    public String toString() {
        return mData.toString();
    }

    /**
     *  扱うデータが存在するか否かの真偽値です
     *  常にtrueを返します
     *  @return true
     */
    public boolean test() {
        return true;
    }

    /**
     *  保持しているデータ(扱っているマッチ文字列全体及び各グルーピング文字列)のイテレータを返します
     */
    public Iterator<String> iterator() {
        return mData.iterator();
    }
}
