package jp.gr.java_conf.falius.util.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  <p>
 *  正規表現を扱うクラス
 *  <p>
 *  newInstance()とmatch()を使用して正規表現とターゲット文字列を渡すことでマッチングを行います<br>
 *  find(int)でマッチ箇所を選択し、group(int)によって該当文字列を取り出すことができます
 *  <p>
 *  正規表現はスラッシュで囲んだ文字列によって表されます   例:"/a (pen)\\./"
 *
 *  <table summary="使用の流れ">
 *  <thead><tr><th colspan="3">
 *  基本的な使用の流れ
 *      ("sample test sample regex"から"sample test"と"sample regex"に正規表現をマッチさせ、
 *      後者から"regex"をグルーピングで取り出す)
 *      </th></tr></thead>
 *  <tr><td>1</td><td>{@code
 *       Regex reg = Regex.newInstance("sample test sample regex");}</td>
 *       <td>正規表現か対象文字列のいずれかを渡してインスタンスを作成する</td></tr>
 *  <tr><td>2</td>
 *  <td>{@code reg.match("/sample (\\S+)/");}</td>
 *       <td>インスタンス作成時に渡さなかった正規表現あるいは対象文字列のいずれかをmatch(CharSequence)に渡す。戻り値は自分自身のインスタンスなので、変数に入れる必要はなし</td></tr>
 *  <tr><td>3</td>
 *  <td>{@code Regex.Data data = reg.find(1);}</td>
 *       <td>マッチ箇所が複数あれば、find(int)で選択する。この例ではふたつ目のマッチ箇所("sample regex")を選択している // Regex.Dataインターフェイスを実装した内部クラスが取り出される</td></tr>
 *  <tr><td>4</td>
 *  <td>{@code String result = data.group(1);}</td>
 *       <td>group(int)で文字列を取り出す。ひとつ目のグループなので１を渡している // resultに"regex"が入る</td></tr>
 *  </table>
 *  <p>
 *  各段階の結果をキャッシュする必要がなければ、メソッドチェーンの形で利用すると便利でしょう。<br>
 *  {@code String result = Regex.newInstance("sample test sample regex").match("/sample (\\S+)/").find(1).group(1);}
 *
 *  <p>
 *  引数の正規表現とターゲット文字列は順不同で渡すことができます<br>
 *  例:{@code Regex.newInstance("this is a pen.").match("/a (pen)\\./")}と{@code Regex.newInstance("/a (pen)\\./").match("this is a pen.")}に違いはありません
 *  <p>
 *  Dataオブジェクトではなく、Regexオブジェクトが直接持つgroup(int)は正規表現を"/regex/g"の形にしたかどうかで戻り値が変化します
 *
 *  <p>オプションは以下の種類があります。また、Patternクラスの埋め込みフラグも別途そのまま利用できます。
 * <table summary="オプションの種類">
 *  <thead><tr><th>種類</th><th>意味</th><th>埋め込みフラグ</th></tr></thead>
 *  <tfoot><tr><td colspan="3">例:gオプションとiオプションを指定する "/regex/gi"または"/(?i)regex/g"</td></tr></tfoot>
 *  <tbody>
 * <tr><td>g</td><td>group()メソッドで取得できる値を変更する</td><td>なし</td></tr>
 * <tr><td>i</td><td>大文字と小文字を区別しない</td><td>(?i)</td></tr>
 * <tr><td>m</td><td>複数行モードを有効にする。$記号が各行末にもマッチする</td><td>(?m)</td></tr>
 * <tr><td>s</td><td>DOTALLモードを有効にする。.(ドット)が改行にもマッチする</td><td>(?s)</td></tr>
 * <tr><td>u</td><td>Unicodeに準拠した大文字と小文字を区別しないマッチング(iオプションと同時指定する必要あり)。例えば全角アルファベットでも大文字小文字を区別しない</td><td>(?u)</td></tr>
 * <tr><td>d</td><td>Unixラインモードを有効にする。複数(\r\nなども)ある改行記号が、\nのみに限定される</td><td>(?d)</td></tr>
 * <tr><td>x</td><td>パターン内で空白とコメントを使用。スペースは無視され、#から行末(文末と改行記号)までコメント扱い</td><td>(?x)</td></tr>
 * <tr><td>l</td><td>パターンのリテラル構文解析を有効にする。.や^など正規表現の特殊文字がすべて通常文字として扱われる</td><td>なし</td></tr>
 * </tbody>
 * </table>
 *
 *
 * <p>
 * 通常iterator()は、group()にて得られる各種文字列のイテレータを取得します<br>
 * groupIterator(int index)を利用することで、各マッチ文字列のグループ化された文字列のイテレータに変更することができます(マッチした文字列すべてのひとつ目のグループ化文字列をイテレートするなど)
 * <pre>{@code
 * Regex regex = Regex.newInstance("sampleSampleSAMPLEsAMpLe").match("/sa(m)ple/gi");
 *
 * for (String result : regex) {
 *  System.out.println(result); // "sample", "Sample", "SAMPLE", "sAMpLe"
 * }
 *
 * for (String result : regex.groupIterator(1)) {
 *  System.out.println(result); // "m", "m", "M", "M" : 上記で取得したマッチ文字列のうちひとつ目のグループ文字列(m)を取得する
 * }
 *
 * for (String result : regex.find(2)) {
 *  System.out.println(result); // "SAMPLE", "M" : Regex.Dataオブジェクトもイテレータを持っている
 *}
 * }</pre>
 */
public class Regex implements Iterable<String> {
    // ひとつの正規表現に対してmatch()によって異なる対象文字列を次々マッチさせていくという使い方を可能にするため、
    // 可変オブジェクトにしている
    // そのため、Mapなどのkeyとしては不適
    // マルチスレッドでのインスタンス共有も不適

    private static final Matcher mSlashMatcher; // スラッシュで囲まれた正規表現の形になっているかどうかを判別する
    private static final String[] EMPTY_STRING_ARRAY = new String[0]; // toArray()で使いまわす空の配列

    private Pattern mPattern = null;
    private Matcher mMatcher = null;
    private String mRegex = null; // 正規表現
    private String mTarget = null; // 調査対象文字列
    private boolean mIsOptionG = false; // gオプションの有無
    private int mPatternFlag = 0; // 有効になっているオプションの値

    private List<PartData> mDataList = new ArrayList<>(); // マッチした各部分文字列を扱うオブジェクトのリスト
    private List<String> mMatchList = new ArrayList<>(); // マッチした各部分文字列のリスト

    private enum ResultCode {
        REGEX_ALL, REGEX, G_OPTION, PATTERN_FLAG;
    };

    static {
        // commentモードでは正規表現中に改行が入ることもあるのでDOTALLモード
        Pattern slashPattern_ = Pattern.compile("^/(.+)/([gimsudxl]*)$", Pattern.DOTALL);
        mSlashMatcher = slashPattern_.matcher("");
    }

    private Regex(String regex, String target, int patternFlag, boolean isOptionG) {
        mRegex = regex;
        mTarget = target;
        mIsOptionG = isOptionG;
        mPatternFlag = patternFlag;
    }

    // 正規表現文字列を解析して、結果をマップで返す
    // 正規表現の形式になっていなければnullを返す
    // ^/(.+)/([gimsudxl]*)$
    private static Map<ResultCode, String> splitStringRegex(CharSequence regexOrTarget) {
        Objects.requireNonNull(regexOrTarget, "regexOrTarget is null");

        mSlashMatcher.reset(regexOrTarget);

        if (!mSlashMatcher.find()) {
            return null;
        }

        Map<ResultCode, String> resultMap = new EnumMap<>(ResultCode.class);
        // 渡された正規表現全体
        resultMap.put(ResultCode.REGEX_ALL, mSlashMatcher.group());
        // 囲んでいるスラッシュやオプション文字列を除いた正規表現の本体
        resultMap.put(ResultCode.REGEX, mSlashMatcher.group(1));

        // オプション各種
        String strOptions = mSlashMatcher.group(2);
        resultMap.put(ResultCode.G_OPTION, strOptions.contains("g") ? "true" : "false"); // gオプションの有無
        Set<Option> options = Option.optionsFrom(strOptions);
        int patternFlag = Option.computePatternFlag(options);
        resultMap.put(ResultCode.PATTERN_FLAG, String.valueOf(patternFlag)); // 修飾子の種類

        return resultMap;
    }

    /**
     *  正規表現とターゲット文字列を渡して新しいインスタンスを作成し、同時にマッチングを行います
     *  引数は順不同です
     *  @param regexOrTarget スラッシュで囲まれた正規表現、あるいはターゲット文字列
     *  @param targetOrRegex ターゲット文字列、あるいはスラッシュで囲まれた正規表現
     *  @return  マッチングを終えた新しいRegexインスタンス
     *  @throws NullPointerException 引数にnullが渡された場合
     *  @throws IllegalArgumentException 引数がスラッシュで囲まれた正規表現形式と囲まれていない通常文字列の組み合わせとなっていなかった場合
     */
    public static Regex newInstance(String regexOrTarget, String targetOrRegex) {
        // スラッシュで囲まれた正規表現形式と囲まれていない通常文字列の組み合わせ以外ならIllegalArgumentException
        return newInstance(regexOrTarget).match(targetOrRegex);
    }

    /**
     *  正規表現あるいはターゲット文字列のどちらかを渡して新しいインスタンスを作成します
     *  その後、match(CharSequence)を使用して渡していない正規表現あるいはターゲット文字列を
     *      インスタンスに渡す必要があります
     *  @param regexOrTarget スラッシュで囲まれた正規表現、あるいはターゲット文字列
     *  @return マッチングを終えていない新しいRegexインスタンス
     *  @throws NullPointerException 引数にnullが渡された場合
     */
    public static Regex newInstance(CharSequence regexOrTarget) {
        Map<ResultCode, String> regexMap = splitStringRegex(regexOrTarget);
        if (regexMap != null) {
            // 引数は正規表現
            boolean optionG = Boolean.parseBoolean(regexMap.get(ResultCode.G_OPTION));
            int patternFlag = Integer.parseInt(regexMap.get(ResultCode.PATTERN_FLAG));
            return new Regex(
                    /* regex = */ regexMap.get(ResultCode.REGEX), /* target = */ null,
                    /* patternFlag = */ patternFlag, /* isOptionG = */ optionG);
        } else {
            // 引数は調査対象文字列
            return new Regex(
                    /* regex = */ null, /* target = */ regexOrTarget.toString(),
                    /* patternFlag */ 0, /* isOptionG = */ false);
        }
    }

    /**
     *  正規表現かターゲット文字列を渡し、マッチングを行います
     *  @param  targetOrRegex ターゲット文字列、あるいはスラッシュで囲まれた正規表現
     *  @return マッチングを終えた自身のインスタンス
     *  @throws NullPointerException 引数にnullが渡された場合
     *  @throws IllegalArgumentException    渡された引数によって正規表現とターゲット文字列が揃わなかった場合
     */
    public Regex match(CharSequence targetOrRegex) {
        // すでに一度マッチさせていたインスタンスを再利用する場合
        if (Objects.nonNull(mMatcher)) {
            return renewMatch(targetOrRegex);
        }

        Map<ResultCode, String> regexMap = splitStringRegex(targetOrRegex);

        if ((Objects.isNull(mRegex) && Objects.isNull(regexMap))
                || (Objects.isNull(mTarget) && Objects.nonNull(regexMap))) {
            throw new IllegalArgumentException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }

        if (Objects.nonNull(regexMap)) {
            // 引数は正規表現
            mRegex = regexMap.get(ResultCode.REGEX);
            mIsOptionG = Boolean.parseBoolean(regexMap.get(ResultCode.G_OPTION));
            mPatternFlag = Integer.parseInt(regexMap.get(ResultCode.PATTERN_FLAG));
        } else {
            // 引数は調査対象文字列
            mTarget = targetOrRegex.toString();
        }

        mPattern = createPattern(mRegex, mPatternFlag);
        mMatcher = mPattern.matcher(mTarget);

        return this.build();
    }

    // 正規表現(スラッシュなし文字列)とパターンフラグ(Patternフィールドの論理和)からPatternを作成する
    private static Pattern createPattern(String regex, int patternFlag) {
        if (patternFlag > 0) {
            return Pattern.compile(regex, patternFlag);
        }
        return Pattern.compile(regex);
    }

    // PatternとMatcherのいずれかのみ変更し、もう一方はそのまま
    private Regex renewMatch(CharSequence targetOrRegex) {
        Map<ResultCode, String> regexMap = splitStringRegex(targetOrRegex);
        if (Objects.nonNull(regexMap)) {
            // 引数は正規表現
            mRegex = regexMap.get(ResultCode.REGEX);
            mIsOptionG = Boolean.parseBoolean(regexMap.get(ResultCode.G_OPTION));
            mPatternFlag = Integer.parseInt(regexMap.get(ResultCode.PATTERN_FLAG));
            mPattern = createPattern(mRegex, mPatternFlag);
            mMatcher = mMatcher.usePattern(mPattern);
        } else {
            // 引数は調査対象文字列
            mTarget = targetOrRegex.toString();
            mMatcher.reset(mTarget);
        }

        return this.build();
    }

    // targetとregexを掛けあわせて、各種データを作成する
    // マッチ文字列がひとつもなければgMatchList_およびmDataListはともに空のListになる
    private Regex build() {
        // 全マッチ部分文字列リストを作成
        createMatchAll(mMatchList);

        // 部分文字列ごとに内部データを作る
        mDataList.clear();
        List<String> temp;
        while (Objects.nonNull(temp = createMatchList(mMatcher))) {
            mDataList.add(new PartData(temp));
        }

        mMatcher.reset();

        return this;
    }

    // matcherを渡されると、次に見つかるマッチ文字列とグループをリストにして返す
    // matcherが終端に到達していてそれ以上マッチ文字列が見つからなければnull
    private List<String> createMatchList(Matcher mat) {
        if (!mat.find()) {
            return null;
        }
        List<String> list = new ArrayList<String>();

        list.add(mat.group()); // マッチ文字列全体

        // matcher.groupCount()は、あくまでグループの数なのでマッチ文字列全体のmatcher.group(0)は含まない
        // グルーピングが0なら、matcher.group(0)も例外を投げる
        String temp;
        for (int i = 1, cnt = mat.groupCount() + 1; i < cnt; i++) {
            temp = mat.group(i);
            list.add(temp);
        }

        return list;
    }

    // targetのうち、正規表現regexに該当する部分を抜き出して配列にする(最初のマッチ部分のみ)
    // targetの全てにおいて、マッチ部分を順に[0]からlistに入れていく
    // マッチ文字列がひとつもなければ空のListになる
    private List<String> createMatchAll(List<String> list) {
        list.clear();

        while (mMatcher.find()) {
            list.add(mMatcher.group());
        }

        mMatcher.reset();
        return list;
    }

    /**
     *  マッチした箇所を表すDataオブジェクトを返します
     *  このメソッドはgオプションの有無に影響されずに利用できます
     *  @param  index   マッチ箇所のインデックス。マッチ箇所が存在した場合、有効範囲は０ ~ matchCount()-1になります
     *  @return マッチ箇所を表すDataオブジェクト
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 引数が有効範囲外の場合
     */
    public PartData find(int index) {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        if (matchCount() == 0) {
            throw new NoSuchElementException("マッチした部分文字列がありません");
        }
        if (index < 0 || index > matchCount() - 1) {
            throw new IndexOutOfBoundsException(
                    String.format("有効なインデックスは0から%dです : passed index=%d", matchCount() - 1, index));
        }

        return mDataList.get(index);
    }

    /**
     *  最初にマッチした箇所の文字列を返します
     *  group(0)と同義です
     *  @return 最初にマッチした箇所の文字列全体
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     */
    public String group() {
        return group(0);
    }

    /**
     *  <p>正規表現の最後にgを付加したか否かで挙動が変わります
     *  <p>gあり:引数で指定されたインデックスのマッチ箇所の全体文字列を返します<br>
     *  gなし:最初にマッチした箇所の指定されたインデックスのグルーピング文字列を返します。
     *      インデックスが0のときは最初にマッチした箇所の文字列全体を返します。find(0).group(index)と同義です
     *  @param  index   マッチ箇所のインデックス(gオプションあり)、あるいはグルーピング箇所のインデックス(gオプションなし)
     *      有効範囲は0からgroupCount()-1
     *  @return 引数で指定されたインデックスのマッチ箇所(gオプションあり)、
     *      あるいは最初にマッチした箇所のグルーピング文字列(gオプションなし)
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 渡されたインデックスが有効範囲外の場合
     */
    public String group(int index) {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        if (matchCount() == 0) {
            throw new NoSuchElementException("マッチした部分文字列がありません");
        }
        if (index < 0 || index > groupCount() - 1) {
            throw new IndexOutOfBoundsException(
                    String.format(
                            "有効なインデックスは0から%dです : passed index=%d : gオプション%s",
                            (groupCount() - 1), index, hasOptionG() ? "あり" : "なし"));
        }

        if (hasOptionG()) {
            return mMatchList.get(index); // 引数で指定された番号の部分文字列がなければnull
        } else {
            return find(0).group(index); // マッチした部分文字列がなければnull
        }
    }

    /**
     *  group(int)によって取得しうる文字列の配列を作成して返します
     *  @return マッチした各文字列の配列(gオプションあり)、
     *      あるいは最初にマッチした箇所の全体文字列と各グルーピング文字列の配列(gオプションなし)
     *      ひとつもマッチしていなければ空の配列
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public String[] toArray() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }

        if (hasOptionG()) {
            return groupCount() > 0 ? mMatchList.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
        } else {
            return mDataList.size() > 0 ? mDataList.get(0).toArray() : EMPTY_STRING_ARRAY;
        }
    }

    /**
     *  マッチ結果を含む文字列表現を返します
     *  @return マッチした各文字列を含む文字列表現(gオプションあり)、あるいは最初にマッチした箇所の全体文字列と各グルーピング文字列を含む文字列表現(gオプションなし)
     */
    @Override
    public String toString() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget) || matchCount() == 0) {
            return Arrays.asList(EMPTY_STRING_ARRAY).toString();
        }
        if (hasOptionG()) {
            return mMatchList.toString();
        } else {
            return find(0).toString();
        }
    }

    /**
     *  マッチした部分文字列の総数を返します
     *  マッチしていなければ0になります
     *  @return マッチした部分文字列の総数
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public int matchCount() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        return mDataList.size();
    }

    /**
     * group(int)メソッドによって取得できる値の総数を返します
     * Regex.DataオブジェクトのgroupCount()とは意味が異なるのでご注意ください
     * @return マッチした部分文字列の数(gオプションあり)、あるいは最初にマッチした部分文字列の扱うデータ数(gオプションなし)
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public int groupCount() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        if (hasOptionG()) {
            return mMatchList.size();
        } else {
            return matchCount() > 0 ? find(0).size() : 0;
        }
    }

    /**
     * 調査対象文字列のマッチ部分を、すべてreplacementで置き換えた文字列を返します
     * @param   replacement 置き換える文字列
     * @return  すべて置き換えた後の文字列
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public String replaceAll(String replacement) {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        return mMatcher.replaceAll(replacement);
    }

    /**
     *  ターゲット文字列に正規表現がマッチしたのかどうかを表す真偽値を返します
     *  matches()とは異なり、一部にでもマッチしていればtrueを返します
     *  @return ひとつでもマッチしていればtrue、そうでなければfalse
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public boolean test() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        return mDataList.size() > 0;
    }

    /**
     *  ターゲット文字列に正規表現がマッチするのかどうかを表す真偽値を返す静的メソッドです
     *  matches()とは異なり、一部にでもマッチしていればtrueを返します
     *  引数は順不同です
     *  @param  regexOrTarget   スラッシュで囲まれた正規表現、あるいはターゲット文字列
     *  @param  targetOrRegex   ターゲット文字列、あるいはスラッシュで囲まれた正規表現
     *  @return マッチするとtrue、そうでなければfalse
     *  @throws NullPointerException 引数にnullが渡された場合
     *  @throws IllegalArgumentException    渡された引数によって正規表現とターゲット文字列が揃わなかった場合
     */
    public static boolean test(CharSequence regexOrTarget, CharSequence targetOrRegex) {
        Objects.requireNonNull(targetOrRegex, "targetOrRegex is null");

        Map<ResultCode, String> regexMap = splitStringRegex(regexOrTarget);
        if (regexMap != null) {
            if (splitStringRegex(targetOrRegex) == null) {
                throw new IllegalArgumentException("ターゲット文字列がありません");
            }
            Pattern pattern = createPattern(
                    regexMap.get(ResultCode.REGEX), Integer.parseInt(regexMap.get(ResultCode.PATTERN_FLAG)));
            Matcher matcher = pattern.matcher(targetOrRegex);
            return matcher.find() ? true : false;
        } else {
            regexMap = splitStringRegex(targetOrRegex);
            if (regexMap == null) {
                throw new IllegalArgumentException("正規表現がありません");
            }
            Pattern pattern = createPattern(regexMap.get(ResultCode.REGEX),
                    Integer.parseInt(regexMap.get(ResultCode.PATTERN_FLAG)));
            Matcher matcher = pattern.matcher(regexOrTarget);
            return matcher.find() ? true : false;
        }
    }

    /**
     *  ターゲット文字列の１文字目から末尾までの全体が正規表現にマッチしているかどうかの真偽値を返します
     *  正規表現の両端に^と$を付与した場合のtest()と同義となります
     *  @return ターゲット文字列の全領域が正規表現にマッチしていればtrue。そうでなければfalse
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public boolean matches() {
        if (Objects.isNull(mRegex) || Objects.isNull(mTarget)) {
            throw new IllegalStateException(
                    mRegex == null ? "正規表現がありません" : "ターゲット文字列がありません");
        }
        return mMatcher.matches();
    }

    /**
     *  ターゲット文字列の１文字目から末尾までの全体が正規表現にマッチしているかどうかの真偽値を返す静的メソッドです
     *  正規表現の両端に^と$を付与した場合のtest(regexOrTarget, targetOrRegex)と同義となります
     *  オプション指定は埋め込みフラグを利用してください
     *  引数は順不同です
     *  @param  regexOrTarget   スラッシュで囲まれた正規表現、あるいはターゲット文字列
     *  @param  targetOrRegex ターゲット文字列、あるいはスラッシュで囲まれた正規表現
     *  @return ターゲット文字列の全領域が正規表現にマッチしていればtrue。そうでなければfalse
     *  @throws NullPointerException 引数にnullが渡された場合
     *  @throws IllegalArgumentException    渡された引数によって正規表現とターゲット文字列が揃わなかった場合
     */
    public static boolean matches(CharSequence regexOrTarget, CharSequence targetOrRegex) {
        Objects.requireNonNull(targetOrRegex, "targetOrRegex is null");
        Map<ResultCode, String> regexMap = splitStringRegex(regexOrTarget);
        Map<ResultCode, String> targetMap = splitStringRegex(targetOrRegex);
        if (regexMap == null && targetMap == null) {
            throw new IllegalArgumentException("正規表現がありません");
        }
        if (regexMap != null && targetMap != null) {
            throw new IllegalArgumentException("ターゲット文字列がありません");
        }

        if (regexMap != null) {
            // regexOrTarget = 正規表現
            return Pattern.matches(regexMap.get(ResultCode.REGEX), targetOrRegex);
        } else {
            // targetOrRegex = 正規表現
            return Pattern.matches(splitStringRegex(targetOrRegex).get(ResultCode.REGEX), regexOrTarget);
        }
    }

    /**
     * 渡されたターゲット文字列を返します
     @return ターゲット文字列。まだ渡されていなければnull
     */
    public String getTarget() {
        return mTarget;
    }

    /**
     *  渡された正規表現を返します
     *  各種オプションや/(スラッシュ)は含まれず、/(スラッシュ)に囲まれた部分のみであることにご注意ください
     *  @return 正規表現。まだ渡されていなければnull
     */
    public String getRegex() {
        return mRegex;
    }

    /**
     * 指定されたオプションを列挙した文字列を作成します
     *     ただし、指定した順序は保証されません
     * @return 指定したオプションの文字列表現
     */
    public String toStringOptions() {
        StringBuilder sb = new StringBuilder();
        if (hasOptionG()) {
            sb.append('g');
        }
        if (Option.hasOption(mPatternFlag, "i")) {
            sb.append('i');
        }
        if (Option.hasOption(mPatternFlag, "m")) {
            sb.append('m');
        }
        if (Option.hasOption(mPatternFlag, "s")) {
            sb.append('s');
        }
        if (Option.hasOption(mPatternFlag, "u")) {
            sb.append('u');
        }
        if (Option.hasOption(mPatternFlag, "d")) {
            sb.append('d');
        }
        if (Option.hasOption(mPatternFlag, "x")) {
            sb.append('x');
        }
        if (Option.hasOption(mPatternFlag, "l")) {
            sb.append('l');
        }

        return sb.toString();
    }

    /**
     *  渡された正規表現にgオプションが指定されていたかどうかの真偽値を返します
     *  @return 渡された正規表現にgオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionG() {
        return mIsOptionG;
    }

    /**
     *  渡された正規表現にiオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にiオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionI() {
        return Option.hasOption(mPatternFlag, "i");
    }

    /**
     *  渡された正規表現にmオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にmオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionM() {
        return Option.hasOption(mPatternFlag, "m");
    }

    /**
     *  渡された正規表現にsオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にsオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionS() {
        return Option.hasOption(mPatternFlag, "s");
    }

    /**
     *  渡された正規表現にuオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にuオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionU() {
        return Option.hasOption(mPatternFlag, "u");
    }

    /**
     *  渡された正規表現にdオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にdオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionD() {
        return Option.hasOption(mPatternFlag, "d");
    }

    /**
     *  渡された正規表現にxオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にxオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionX() {
        return Option.hasOption(mPatternFlag, "x");
    }

    /**
     *  渡された正規表現にlオプションが指定されていたかどうかの真偽値を返します
     *  @return 渡された正規表現の/(スラッシュ)の外側にlオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionL() {
        return Option.hasOption(mPatternFlag, "l");
    }

    /**
     *  イテレータを返します
     *  gオプションの有無によってイテレータの内容は変化します
     *  @return 各マッチ文字列のイテレータ(gオプションあり)、<br>
     *  あるいは最初にマッチした部分文字列とそのグルーピング文字列のイテレータ(gオプションなし)、<br>
     */
    public Iterator<String> iterator() {
        if (hasOptionG()) {
            return mMatchList.iterator(); // 各マッチ文字列をイテレートする
        } else {
            return find(0).iterator(); // 最初のマッチ文字列とそのグルーピング文字列をイテレートする
        }
    }

    /**
     *  各マッチ文字列の指定したグルーピング文字列をイテレートするためのIteratorを作成して返します
     *  @param  index   イテレートするグルーピングのインデックス
     *     0にするとgオプションを設定した場合と同様の結果を返すイテレータになる
     *  @return 新しいIterableかつIterator
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 渡されたインデックスが有効範囲外の場合
     */
    public GroupIterator groupIterator(int index) {
        if (matchCount() == 0) {
            throw new NoSuchElementException("マッチした部分文字列がありません");
        }
        if (index < 0 || index > groupCount() - 1) {
            throw new IndexOutOfBoundsException(
                    String.format("有効なインデックスは0から%dです : passed index=%d", groupCount() - 1, index));
        }
        return new GroupIterator(index, this);
    }

}
