package jp.gr.java_conf.falius.util.regex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

enum Option {
    CASE_INSENSITIVE("i", Pattern.CASE_INSENSITIVE, "大文字と小文字を区別しない "),
    MULTILINE("m", Pattern.MULTILINE, "復数行モード "),
    DOTALL("s", Pattern.DOTALL, "DOTALLモード "),
    UNICODE_CASE("u", Pattern.UNICODE_CASE, "Unicodeに準拠した大文字と小文字を区別しないマッチング "),
    UNIX_LINES("d", Pattern.UNIX_LINES, "Unixラインモード "),
    COMMENTS("x", Pattern.COMMENTS, "パターン内で空白とコメントを使用 "),
    LITERAL("l", Pattern.LITERAL, "パターンのリテラル構文解析を有効 ");

    public static Set<Option> optionsFrom(String strOptions) {
        Set<Option> ret = new HashSet<>();
        for (Option option : values()) {
            if (strOptions.contains(option.mStr)) {
                ret.add(option);
            }
        }
        return ret;
    }

    // オプションの有無から、パターンフラグ(Patternフィールドの論理和)を計算する
    public static int computePatternFlag(Set<Option> options) {
        int ret = 0;
        for (Option option : options) {
            ret |= option.code();
        }
        return ret;
    }

    public static boolean hasOption(int patternFlag, String option) {
        Option op = from(option);
        if (Objects.isNull(op)) {
            return false;
        }
        return isInclude(patternFlag, op);
    }

    // pattenFlagにオプションが含まれるか否かをビット演算で判定する
    public static boolean isInclude(int patternFlag, Option option) {
        return (patternFlag & option.code()) == option.code();
    }

    public static Option from(String str) {
        return sStringToEnum.get(str);
    }

    private final static Map<String, Option> sStringToEnum = new HashMap<>();

    static {
        for (Option option : values()) {
            sStringToEnum.put(option.mStr, option);
        }
    }

    private final String mStr;
    private final int mCode;
    private final String mDescription;

    Option(String str, int code, String description) {
        mStr = str;
        mCode = code;
        mDescription = description;
    }

    public int code() {
        return mCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mStr)
                .append(':')
                .append(mDescription);
        return sb.toString();
    }
}