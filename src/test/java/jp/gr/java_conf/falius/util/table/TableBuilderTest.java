package jp.gr.java_conf.falius.util.table;

import org.junit.Test;

public class TableBuilderTest {

    @Test
    public void workout() {
        System.out.println("--- work out ---");
        TableBuilder tb = new TableBuilder("名前", "性別", "年齢", "イニシャル");
        // insert(String)で挿入行を指定して内部クラスのインスタンスを取得
        // さらに続けてadd(String, Object)で挿入行とデータを渡す
        tb.insert("Anna")
                .add("性別", "女")
                .add("年齢", 16);
        tb.insert("Alex")
                .add("性別", "男")
                .add("年齢", 21);
        tb.insert("Kai")
                .add(1, "男")
                .add(2, 12)
                .add(0, "カイ"); // 最初の行の値を変更すると、識別名とは別に変更される
        Object alex = new Object() {
            String name = "Alex";

            public String toString() {
                return name;
            }
        };
        tb.insert("Anna").add(3, 'A');
        // オブジェクトを行の識別名として使用することもできる。特に一行目の名前を指定していなければtoString()の戻り値が行名として使われる
        tb.insert(alex).add("イニシャル", "A");
        tb.insert("Kai").add(3, 'K');
        tb.println();
        System.out.println("--- work out ---");
    }

    @Test
    public void toStringTest() {
        System.out.println("--- toString ---");
        TableBuilder tb = new TableBuilder("名前", "性別", "年齢", "イニシャル");
        // insert(String)で挿入行を指定して内部クラスのインスタンスを取得
        // さらに続けてadd(String, Object)で挿入行とデータを渡す
        tb.insert("Anna")
                .add("性別", "女")
                .add("年齢", 16);
        tb.insert("Alex")
                .add("性別", "男")
                .add("年齢", 21);
        tb.insert("Kai")
                .add(1, "男")
                .add(2, 12)
                .add(0, "カイ"); // 最初の行の値を変更すると、識別名とは別に変更される
        Object alex = new Object() {
            String name = "Alex";

            public String toString() {
                return name;
            }
        };
        tb.insert("Anna").add(3, 'A');
        // オブジェクトを行の識別名として使用することもできる。特に一行目の名前を指定していなければtoString()の戻り値が行名として使われる
        tb.insert(alex).add("イニシャル", "A");
        tb.insert("Kai").add(3, 'K');
        System.out.println(tb.toString());

        System.out.println("--- toString ---");
    }
}
