package jp.gr.java_conf.falius.util.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  <p>正規表現を扱うクラス
 *  <p>newInstance()とmatch()を使用して正規表現とターゲット文字列を渡すことでマッチングを行います<br>
 *  find(int)でマッチ箇所を選択し、group(int)によって該当文字列を取り出すことができます
 *  <p>正規表現はスラッシュで囲んだ文字列によって表されます   例:"/a (pen)\\./"
 *  <table summary="使用の流れ">
 *  <thead><tr><th colspan="3">基本的な使用の流れ("sample test sample regex"から"sample test"と"sample regex"に正規表現をマッチさせ、後者から"regex"をグルーピングで取り出す)</th></tr></thead>
 *  <tfoot><tr><td colspan="3">各段階の結果をキャッシュする必要がなければ、メソッドチェーンの形で利用すると便利でしょう。{@code String result = Regex.newInstance("sample test sample regex").match("/sample (\\S+)/").find(1).group(1);}</td><tr></tfoot>
 *  <tr><td>1</td><td>{@code Regex reg = Regex.newInstance("sample test sample regex");}</td><td>正規表現か対象文字列のいずれかを渡してインスタンスを作成する</td></tr>
 *  <tr><td>2</td><td>{@code reg.match("/sample (\\S+)/");}</td><td>インスタンス作成時に渡さなかった正規表現あるいは対象文字列のいずれかをmatch(CharSequence)に渡す。戻り値は自分自身のインスタンスなので、変数に入れる必要はなし</td></tr>
 *  <tr><td>3</td><td>{@code Regex.Data data = reg.find(1);}</td><td>マッチ箇所が複数あれば、find(int)で選択する。この例ではふたつ目のマッチ箇所("sample regex")を選択している // Regex.Dataインターフェイスを実装した内部クラスが取り出される</td></tr>
 *  <tr><td>4</td><td>{@code String result = data.group(1);}</td><td>group(int)で文字列を取り出す。ひとつ目のグループなので１を渡している // resultに"regex"が入る</td></tr>
 *  </table>
 *  <p>引数の正規表現とターゲット文字列は順不同で渡すことができます<br>
 *  例:{@code Regex.newInstance("this is a pen.").match("/a (pen)\\./")}と{@code Regex.newInstance("/a (pen)\\./").match("this is a pen.")}に違いはありません
 *  <p>Dataオブジェクトではなく、Regexオブジェクトが直接持つgroup(int)は正規表現を"/regex/g"の形にしたかどうかで戻り値が変化します
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
 *  System.out.println(result); // "sample","Sample","SAMPLE","sAMpLe"
 * }
 *
 * for (String result : regex.groupIterator(1)) {
 *  System.out.println(result); // "m","m","M","M" : 上記で取得したマッチ文字列のうちひとつ目のグループ文字列(m)を取得する
 * }
 *
 * for (String result : regex.find(2)) {
 *  System.out.println(result); // "SAMPLE","M" : Regex.Dataオブジェクトもイテレータを持っている
 *}
 * }</pre>
 */
public class Regex implements Iterable<String> {
    // ひとつの正規表現に対してmatch()によって異なる対象文字列を次々マッチさせていくという使い方を可能にするため、可変オブジェクトにしている
    // そのため、Mapなどのkeyとしては不適
    // マルチスレッドでのインスタンス共有も不適
    private Pattern pattern_ = null;
    private Matcher matcher_ = null;
    private String regex_ = null; // 正規表現
    private String target_ = null; // 調査対象文字列
    private boolean isOptionG_ = false; // gオプションの有無
    private int patternFlag_ = 0; // 有効になっているオプションの値

    private List<Data> dataList_ = new ArrayList<>(); // マッチした各部分文字列を扱うオブジェクトのリスト
    private List<String> gMatchList_ = new ArrayList<>(); // マッチした各部分文字列のリスト

    private static final Matcher slashMatcher_; // スラッシュで囲まれた正規表現の形になっているかどうかを判別する
    private static final String[] EMPTY_STRING_ARRAY = new String[0]; // toArray()で使いまわす空の配列

    static {
        Pattern slashPattern_ = Pattern.compile("^/(.+)/([gimsudxl]*)$", Pattern.DOTALL); // commentモードでは正規表現中に改行が入ることもあるのでDOTALLモード
        slashMatcher_ = slashPattern_.matcher("");
    }

    private Regex(String regex,String target, int patternFlag, boolean isOptionG) {
        this.regex_ = regex;
        this.target_ = target;
        this.isOptionG_ = isOptionG;
        this.patternFlag_ = patternFlag;
    }

    // 正規表現文字列を解析して、結果をマップで返す
    // 正規表現の形式になっていなければnullを返す
    // ^/(.+)/([gimsudxl]*)$
    private static Map<String,String> splitStringRegex(CharSequence regexOrTarget) {
        Objects.requireNonNull(regexOrTarget, "regexOrTarget is null");

        slashMatcher_.reset(regexOrTarget);

        if (!slashMatcher_.find()) return null;

        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("regexAll",slashMatcher_.group()); // 渡された正規表現全体
        resultMap.put("regex",slashMatcher_.group(1)); // 囲んでいるスラッシュやオプション文字列を除いた正規表現の本体

        // オプション各種
        String strOption = slashMatcher_.group(2);
        resultMap.put("g_option", strOption.contains("g") ? "true" : "false"); // gオプションの有無
        boolean isOptionI = strOption.contains("i"); // 大文字と小文字を区別しない
        boolean isOptionM = strOption.contains("m"); // 復数行モード
        boolean isOptionS = strOption.contains("s"); // DOTALLモード
        boolean isOptionU = strOption.contains("u"); // Unicodeに準拠した大文字と小文字を区別しないマッチング
        boolean isOptionD = strOption.contains("d"); // Unixラインモード
        boolean isOptionX = strOption.contains("x"); // パターン内で空白とコメントを使用
        boolean isOptionL = strOption.contains("l"); // パターンのリテラル構文解析を有効
        int patternFlag = computePatternFlag(isOptionI, isOptionM, isOptionS, isOptionU, isOptionD, isOptionX, isOptionL);
        resultMap.put("pattern_flag", String.valueOf(patternFlag)); // 修飾子の種類

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
    public static Regex newInstance(String regexOrTarget,String targetOrRegex) {
        return newInstance(regexOrTarget).match(targetOrRegex); // スラッシュで囲まれた正規表現形式と囲まれていない通常文字列の組み合わせ以外ならIllegalArgumentException
    }

    /**
     *  正規表現あるいはターゲット文字列のどちらかを渡して新しいインスタンスを作成します
     *  その後、match(CharSequence)を使用して渡していない正規表現あるいはターゲット文字列をインスタンスに渡す必要があります
     *  @param regexOrTarget スラッシュで囲まれた正規表現、あるいはターゲット文字列
     *  @return マッチングを終えていない新しいRegexインスタンス
     *  @throws NullPointerException 引数にnullが渡された場合
     */
    public static Regex newInstance(CharSequence regexOrTarget) {
        Map<String, String> regexMap =  splitStringRegex(regexOrTarget);
        if (regexMap != null) {
            // 引数は正規表現
            boolean optionG = Boolean.parseBoolean(regexMap.get("g_option"));
            int patternFlag = Integer.parseInt(regexMap.get("pattern_flag"));
            return new Regex(/* regex = */ regexMap.get("regex"),/* target = */ null,/* patternFlag = */ patternFlag,/* isOptionG = */ optionG);
        } else {
            // 引数は調査対象文字列
            return new Regex(/* regex = */ null,/* target = */ regexOrTarget.toString(),/* patternFlag */ 0,/* isOptionG = */ false);
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
        if (matcher_ != null) return renewMatch(targetOrRegex); // すでに一度マッチさせていたインスタンスを再利用する場合

        Map<String, String> regexMap =  splitStringRegex(targetOrRegex);

        if ((regex_ == null && regexMap == null) || (target_ == null && regexMap != null))
            throw new IllegalArgumentException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");

        if (regexMap != null) {
            // 引数は正規表現
            regex_ = regexMap.get("regex");
            isOptionG_ = Boolean.parseBoolean(regexMap.get("g_option"));
            patternFlag_ = Integer.parseInt(regexMap.get("pattern_flag"));
        } else {
            // 引数は調査対象文字列
            target_ = targetOrRegex.toString();
        }

        pattern_ = createPattern(regex_,patternFlag_);
        matcher_ = pattern_.matcher(target_);

        return this.build();
    }

    // 正規表現(スラッシュなし文字列)とパターンフラグ(Patternフィールドの論理和)からPatternを作成する
    private static Pattern createPattern(String regex,int patternFlag) {
        if (patternFlag > 0)
            return Pattern.compile(regex, patternFlag);
        return Pattern.compile(regex);
    }

    // オプションの有無から、パターンフラグ(Patternフィールドの論理和)を計算する
    private static int computePatternFlag(boolean isOptionI, boolean isOptionM, boolean isOptionS, boolean isOptionU, boolean isOptionD, boolean isOptionX, boolean isOptionL) {
        int patternFlag = 0;
        if (isOptionI) { patternFlag |= Pattern.CASE_INSENSITIVE; }
        if (isOptionM) { patternFlag |= Pattern.MULTILINE; }
        if (isOptionS) { patternFlag |= Pattern.DOTALL; }
        if (isOptionU) { patternFlag |= Pattern.UNICODE_CASE; }
        if (isOptionD) { patternFlag |= Pattern.UNIX_LINES; }
        if (isOptionX) { patternFlag |= Pattern.COMMENTS; }
        if (isOptionL) { patternFlag |= Pattern.LITERAL; }
        return patternFlag;
    }

    // PatternとMatcherのいずれかのみ変更し、もう一方はそのまま
    private Regex renewMatch(CharSequence targetOrRegex) {
        Map<String, String> regexMap =  splitStringRegex(targetOrRegex);
        if (regexMap != null) {
            // 引数は正規表現
            regex_ = regexMap.get("regex");
            isOptionG_ = Boolean.parseBoolean(regexMap.get("g_option"));
            patternFlag_ = Integer.parseInt(regexMap.get("pattern_flag"));
            pattern_ = createPattern(regex_, patternFlag_);
            matcher_ = matcher_.usePattern(pattern_);
        } else {
            // 引数は調査対象文字列
            target_ = targetOrRegex.toString();
            matcher_.reset(target_);
        }

        return this.build();
    }

    // targetとregexを掛けあわせて、各種データを作成する
    // マッチ文字列がひとつもなければgMatchList_およびdataList_はともに空のListになる
    private Regex build() {
        // 全マッチ部分文字列リストを作成
        createMatchAll(gMatchList_);

        // 部分文字列ごとに内部データを作る
        dataList_.clear();
        List<String> temp;
        while ((temp = createMatchList(matcher_)) != null) {
            dataList_.add(new Data(temp));
        }

        matcher_.reset();

        return this;
    }

    // matcherを渡されると、次に見つかるマッチ文字列とグループをリストにして返す
    // matcherが終端に到達していてそれ以上マッチ文字列が見つからなければnull
    private List<String> createMatchList(Matcher mat) {
        if (!mat.find()) return null;
        List<String> list = new ArrayList<String>();

        list.add(mat.group()); // マッチ文字列全体

        // matcher.groupCount()は、あくまでグループの数なのでマッチ文字列全体のmatcher.group(0)は含まない
        // グルーピングが0なら、matcher.group(0)も例外を投げる
        String temp;
        for (int i = 1,cnt = mat.groupCount() + 1; i < cnt; i++) {
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

        while (matcher_.find()) {
            list.add(matcher_.group());
        }

        matcher_.reset();
        return list;
    }

    /**
     *  マッチした箇所を表すDataオブジェクトを返します
     *  このメソッドはgオプションの有無に影響されずに利用できます
     *  @param  index   マッチ箇所のインデックス。マッチ箇所が存在した場合、有効範囲は０ 縲・matchCount()-1になります
     *  @return マッチ箇所を表すDataオブジェクト
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 引数が有効範囲外の場合
     */
    public Data find(int index) {
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        if (matchCount() == 0)
            throw new NoSuchElementException("マッチした部分文字列がありません");
        if (index < 0 || index > matchCount()-1)
            throw new IndexOutOfBoundsException(String.format("有効なインデックスは0から%dです : passed index=%d", matchCount()-1, index));

        return dataList_.get(index);
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
     *  gなし:最初にマッチした箇所の指定されたインデックスのグルーピング文字列を返します。インデックスが0のときは最初にマッチした箇所の文字列全体を返します。find(0).group(index)と同義です
     *  @param  index   マッチ箇所のインデックス(gオプションあり)、あるいはグルーピング箇所のインデックス(gオプションなし)
     *      有効範囲は0からgroupCount()-1
     *  @return 引数で指定されたインデックスのマッチ箇所(gオプションあり)、あるいは最初にマッチした箇所のグルーピング文字列(gオプションなし)
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 渡されたインデックスが有効範囲外の場合
     */
    public String group(int index) {
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        if (matchCount() == 0)
            throw new NoSuchElementException("マッチした部分文字列がありません");
        if (index < 0 || index > groupCount()-1)
            throw new IndexOutOfBoundsException(String.format("有効なインデックスは0から%dです : passed index=%d : gオプション%s", (groupCount()-1), index, hasOptionG() ? "あり" : "なし"));

        if (hasOptionG()) {
            return gMatchList_.get(index); // 引数で指定された番号の部分文字列がなければnull
        } else {
            return find(0).group(index); // マッチした部分文字列がなければnull
        }
    }

    /**
     *  group(int)によって取得しうる文字列の配列を作成して返します
     *  @return マッチした各文字列の配列(gオプションあり)、あるいは最初にマッチした箇所の全体文字列と各グルーピング文字列の配列(gオプションなし)
     *      ひとつもマッチしていなければ空の配列
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public String[] toArray() {
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");

        if (hasOptionG()) {
            return groupCount() > 0 ? gMatchList_.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
        } else {
            return dataList_.size() > 0 ? dataList_.get(0).toArray() : EMPTY_STRING_ARRAY;
        }
    }

    /**
     *  マッチ結果を含む文字列表現を返します
     *  @return マッチした各文字列を含む文字列表現(gオプションあり)、あるいは最初にマッチした箇所の全体文字列と各グルーピング文字列を含む文字列表現(gオプションなし)
     */
    @Override
    public String toString()  {
        if (regex_ == null || target_ == null || matchCount() == 0) return Arrays.asList(EMPTY_STRING_ARRAY).toString();
        if (hasOptionG()) {
            return gMatchList_.toString();
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
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        return dataList_.size();
    }

    /**
     * group(int)メソッドによって取得できる値の総数を返します
     * Regex.DataオブジェクトのgroupCount()とは意味が異なるのでご注意ください
     * @return マッチした部分文字列の数(gオプションあり)、あるいは最初にマッチした部分文字列の扱うデータ数(gオプションなし)
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public int groupCount() {
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        if (hasOptionG()) {
            return gMatchList_.size();
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
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        return matcher_.replaceAll(replacement);
    }

    /**
     *  ターゲット文字列に正規表現がマッチしたのかどうかを表す真偽値を返します
     *  matches()とは異なり、一部にでもマッチしていればtrueを返します
     *  @return ひとつでもマッチしていればtrue、そうでなければfalse
     *  @throws java.lang.IllegalStateException 正規表現とターゲット文字列が揃っていない状態で呼び出された場合
     */
    public boolean test() {
        if (regex_ == null || target_ == null)
            throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        return dataList_.size() > 0;
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

        Map<String, String> regexMap =  splitStringRegex(regexOrTarget);
        if (regexMap != null) {
            if (splitStringRegex(targetOrRegex) == null) throw new IllegalArgumentException("ターゲット文字列がありません");
            Pattern pattern = createPattern(regexMap.get("regex"),Integer.parseInt(regexMap.get("pattern_flag")));
            Matcher matcher = pattern.matcher(targetOrRegex);
            return matcher.find() ? true : false;
        } else {
            regexMap =  splitStringRegex(targetOrRegex);
            if (regexMap == null) throw new IllegalArgumentException("正規表現がありません");
            Pattern pattern = createPattern(regexMap.get("regex"),Integer.parseInt(regexMap.get("pattern_flag")));
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
        if (regex_ == null || target_ == null) throw new IllegalStateException(regex_ == null ? "正規表現がありません" : "ターゲット文字列がありません");
        return matcher_.matches();
    }

    /**
     *  ターゲット文字列の１文字目から末尾までの全体が正規表現にマッチしているかどうかの真偽値を返す静的メソッドです
     *  正規表現の両端に^と$を付与した場合のtest(regexOrTarget,targetOrRegex)と同義となります
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
        Map<String, String> regexMap =  splitStringRegex(regexOrTarget);
        Map<String, String> targetMap = splitStringRegex(targetOrRegex);
        if (regexMap == null && targetMap == null)
            throw new IllegalArgumentException("正規表現がありません");
        if (regexMap != null && targetMap != null)
            throw new IllegalArgumentException("ターゲット文字列がありません");

        if (regexMap != null) {
            // regexOrTarget = 正規表現
            return Pattern.matches(regexMap.get("regex"),targetOrRegex);
        } else {
            // targetOrRegex = 正規表現
            return Pattern.matches(splitStringRegex(targetOrRegex).get("regex"),regexOrTarget);
        }
    }

    /**
     * 渡されたターゲット文字列を返します
     @return ターゲット文字列。まだ渡されていなければnull
     */
    public String getTarget() {
        return target_;
    }
    /**
     *  渡された正規表現を返します
     *  各種オプションや/(スラッシュ)は含まれず、/(スラッシュ)に囲まれた部分のみであることにご注意ください
     *  @return 正規表現。まだ渡されていなければnull
     */
    public String getRegex() {
        return regex_;
    }
    /**
     * 指定されたオプションを列挙した文字列を作成します
     *     ただし、指定した順序は保証されません
     * @return 指定したオプションの文字列表現
     */
    public String toStringOptions() {
        StringBuilder sb = new StringBuilder();
        if (hasOptionG()) { sb.append('g'); }
        if (hasOption(Pattern.CASE_INSENSITIVE)) { sb.append('i'); }
        if (hasOption(Pattern.MULTILINE)) { sb.append('m'); }
        if (hasOption(Pattern.DOTALL)) { sb.append('s'); }
        if (hasOption(Pattern.UNICODE_CASE)) { sb.append('u'); }
        if (hasOption(Pattern.UNIX_LINES)) { sb.append('d'); }
        if (hasOption(Pattern.COMMENTS)) { sb.append('x'); }
        if (hasOption(Pattern.LITERAL)) { sb.append('l'); }

        return sb.toString();
    }
    /**
     *  渡された正規表現にgオプションが指定されていたかどうかの真偽値を返します
     *  @return 渡された正規表現にgオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionG() {
        return isOptionG_;
    }
    /**
     *  渡された正規表現にiオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にiオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionI() {
        return hasOption(Pattern.CASE_INSENSITIVE);
    }
    /**
     *  渡された正規表現にmオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にmオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionM() {
        return hasOption(Pattern.MULTILINE);
    }
    /**
     *  渡された正規表現にsオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にsオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionS() {
        return hasOption(Pattern.DOTALL);
    }
    /**
     *  渡された正規表現にuオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にuオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionU() {
        return hasOption(Pattern.UNICODE_CASE);
    }
    /**
     *  渡された正規表現にdオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にdオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionD() {
        return hasOption(Pattern.UNIX_LINES);
    }
    /**
     *  渡された正規表現にxオプションが指定されていたかどうかの真偽値を返します
     *  埋め込みフラグは反映されません
     *  @return 渡された正規表現の/(スラッシュ)の外側にxオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionX() {
        return hasOption(Pattern.COMMENTS);
    }
    /**
     *  渡された正規表現にlオプションが指定されていたかどうかの真偽値を返します
     *  @return 渡された正規表現の/(スラッシュ)の外側にlオプションが指定されていればtrue。それ以外はfalse
     */
    public boolean hasOptionL() {
        return hasOption(Pattern.LITERAL);
    }

    // フィールドのpatternFlag_にpattenFlagが含まれるか否かをビット演算で判定する
    private boolean hasOption(int patternFlag) {
        return (patternFlag_ & patternFlag) == patternFlag;
    }

    /**
     *  イテレータを返します
     *  gオプションの有無によってイテレータの内容は変化します
     *  @return 各マッチ文字列のイテレータ(gオプションあり)、<br>
     *  あるいは最初にマッチした部分文字列とそのグルーピング文字列のイテレータ(gオプションなし)、<br>
     */
    public Iterator<String> iterator() {
        if (hasOptionG()) {
            return gMatchList_.iterator(); // 各マッチ文字列をイテレートする
        } else {
            return find(0).iterator(); // 最初のマッチ文字列とそのグルーピング文字列をイテレートする
        }
    }

    /**
     *  各マッチ文字列の指定したグルーピング文字列をイテレートするためのIteratorを作成して返します
     *  @param  index   イテレートするグルーピングのインデックス　0にするとgオプションを設定した場合と同様の結果を返すイテレータになる
     *  @return 新しいIterableかつIterator
     *  @throws NoSuchElementException マッチした部分文字列がない(matchCount() == 0)場合
     *  @throws IndexOutOfBoundsException 渡されたインデックスが有効範囲外の場合
     */
    public GroupIterator groupIterator(int index) {
        if (matchCount() == 0)
            throw new NoSuchElementException("マッチした部分文字列がありません");
        if (index < 0 || index > groupCount()-1)
            throw new IndexOutOfBoundsException(String.format("有効なインデックスは0から%dです : passed index=%d", groupCount()-1, index));
        return new GroupIterator(index, this);
    }

    /**
     *  各マッチ文字列の指定されたインデックスのグルーピング文字列をイテレートするIterableであり、Iteratorです
     */
    public static class GroupIterator implements Iterator<String>, Iterable<String> {
        private final int index;
        private final int size;
        private final Regex regex;
        private int indexCounter = -1;

        private GroupIterator(int index,Regex regex) {
            this.index = index;
            this.regex = regex;
            this.size = regex.matchCount();
        }

        /**
         *  次の要素の有無を返します
         */
        public boolean hasNext() {
            return indexCounter + 1 < size;
        }

        /**
         *  次の要素を返します
         */
        public String next() {
            indexCounter++;
            return regex.find(indexCounter).group(index);
        }

        /**
         *  自身を返します
         */
        public Iterator<String> iterator() {
            return this;
        }
    }

    /**
     *  <p>正規表現にマッチした各部分文字列を扱う内部クラスです
     *  <p>Regexインスタンスからfind(int)に有効値を渡すことで取り出せます
     *  <p>Dataインスタンスのgroup(int)を使うことで扱っている部分文字列全体とグルーピングした文字列を取り出せます
     */
    public static class Data implements Iterable<String> {
        // data.get(0) : 扱う部分文字列全体
        // data.get(1) : 扱う部分文字列のうちグループ化された文字列のひとつ目
        private final List<String>  data;

        private Data(List<String> data) {
            this.data = new ArrayList<String>(data);
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
            if (index < 0 || index > size()-1)
                throw new IndexOutOfBoundsException(String.format("有効なインデックスは0から%dです : passed index=%d", size()-1, index));
            return this.data.get(index);
        }

        /**
         *  扱うデータの数を返します
         *  全体文字列とグルーピングの数の合計なので、groupCount()+1と同義です
         *  @return 扱うデータの数
         */
        public int size() {
            return this.data.size();
        }
        /**
         *  グルーピングの数を返します
         *  @return グルーピングの数
         */
        public int groupCount() {
            return this.data.size() - 1;
        }

        /**
         *  保持しているデータを配列にして返します
         *  @return 扱うデータの配列。[0]にマッチ文字列全体が入り、[1]にひとつ目のグループを表す文字列が入る
         */
        public String[] toArray() {
            return this.data.toArray(EMPTY_STRING_ARRAY);
        }

        /**
         *  扱うデータの文字列表現を返します
         */
        @Override
        public String toString()  {
            return this.data.toString();
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
            return data.iterator();
        }
    }


    // 以下はテストコード
    /*
    public static void main(String[] args) {
        // Regexインスタンスに直接group()を使用した場合
        // 調査対象文字列は一度だけ渡し、正規表現を複数回渡すことで調査対象文字列を使いまわす
        System.out.println(testAll_RegexGroup_recycleTarget("this is an apple. I am hungry. this is a pen. I should study." // target
                    , new String[]{"/this is (a|an) \\S+\\./","/[^\\.]+\\./g","/i[^y.]*y/gi"} // regexes
                    , new String[][]{{"this is an apple.","an"},{"this is an apple."," I am hungry."," this is a pen."," I should study."},{"I am hungry","I should study"}} // expected
                    ));

        // find()してからのgroup()
        // 正規表現は一度だけ渡し、調査対象文字列は複数回渡すことで正規表現を使いまわす
        System.out.println(testAll_DataGroup_recycleRegex("/this is (a|an) (\\S+)\\./" // regex
                    , new String[]{"this is an apple. this is a pen.","this is a cup. this is a smartphone."} // targets
                    , new String[][][]{{{"this is an apple.","an","apple"},{"this is a pen.","a","pen"}},{{"this is a cup.","a","cup"},{"this is a smartphone.","a","smartphone"}}} // expected
                    ));

        // newInstanceに正規表現と調査対象文字列を同時に渡す
        // また、正規表現と調査対象文字列が順不同であることも同時に確認
        System.out.println(testAll_OnceNewInstance_DataGroup("/this is (a|an) (\\S+)\\./" // regex
                    , "this is an apple. I am hungry. this is a pen. I should study." // target
                    , new String[][]{{"this is an apple.","an","apple"},{"this is a pen.","a","pen"}} // expected
                    ));

        // group(),find().group()すべてを確認
        System.out.println(testAll_Both_Regex(
                    Regex.newInstance("/'(\\S+) (green)'/g").match("there are many colors. I like 'lite green','yellow green' and 'dark green'.") // Regex reg
                    , new String[]{"'lite green'","'yellow green'","'dark green'"} // expected_regex
                    , new String[][]{{"'lite green'","lite","green"},{"'yellow green'","yellow","green"},{"'dark green'","dark","green"}} // expected_data
                    ));

        // メソッドチェーン
        // 1:newInstance()に正規表現を渡してインスタンスを作成
        // 2:match()にターゲット文字列を渡してマッチング
        // 3:find(2)でインデックスが２(０から始まるので３番目)の部分文字列を扱うDataオブジェクトを取得
        // 4:group(1)で１つ目のグループの文字列を取得
        // 結果として、"dark"を取得する
        System.out.println(
                Regex.newInstance("/'(\\S+) (green)'/g").match("there are many colors. example,'lite green','orange red','yellow green','sky blue' and 'dark green'.").find(2).group(1).equals("dark")
                );

        System.out.println("-----------------------------------------------------------");
        // test()
        System.out.println("test()");
        System.out.println(Regex.newInstance("there are many greens. I like 'lite green','yellow green' and 'dark green'.","/yellow green/").test());
        System.out.println(Regex.newInstance("there are many greens. I like 'lite green','yellow green' and 'dark green'.").match("/\\S+ green'/").find(1).test()); // ２つ目のマッチ文字列が存在する
        try {
            System.out.println(Regex.newInstance("there are many greens. I like 'lite green','yellow green' and 'dark green'.").match("/\\S+ green'/").find(3).test()); // ４つ目のマッチ文字列が存在しない
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ここでは例外発生が正しい");
            e.printStackTrace();
        }
        System.out.println(Regex.test("there are many greens. I like 'lite green','yellow green' and 'dark green'.","/yellow green/"));

        System.out.println("-----------------------------------------------------------");
        // matches()
        System.out.println("matches()");
        System.out.println(Regex.newInstance("Test","/t.st/i").matches());
        System.out.println(!Regex.newInstance("tTest","/t.st/i").matches()); // falseが正しいので反転
        System.out.println(Regex.newInstance("tTest","/t.st/i").test()); // 同様の組み合わせでも、test()ならtrue
        System.out.println(Regex.matches("Test","/(?i)t.st/"));

        System.out.println("-----------------------------------------------------------");
        // option
        System.out.println("option");
        // gi
        System.out.println(testAll_OnceNewInstance(
                    "/(tes)t/gi" // regex
                    , "testTestTESTtEst" // target
                    , new String[]{"test","Test","TEST","tEst"} // expected_regex
                    , new String[][]{{"test","tes"},{"Test","Tes"},{"TEST","TES"},{"tEst","tEs"}} // expected_data
                    ));
        // inner flag
        System.out.println(testAll_OnceNewInstance(
                    "/(?i)(tes)t/g" // regex
                    , "testTestTESTtEst" // target
                    , new String[]{"test","Test","TEST","tEst"} // expected_regex
                    , new String[][]{{"test","tes"},{"Test","Tes"},{"TEST","TES"},{"tEst","tEs"}} // expected_data
                    ));
        // multiline
        System.out.println(testAll_OnceNewInstance(
                    "/^this is (a|an) (\\S+)\\./m" // regex
                    , "this is an apple. I am hungry. this is a orage. this is a pen. I should study.I do not study.\\nthis is a computergame." // target
                    , new String[]{"this is an apple.","an","apple"} // expected_regex
                    , new String[][]{{"this is an apple.","an","apple"},{"this is a computergame.","a","computergame"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/(?m)^this is (a|an) (\\S+)\\./" // regex
                    , "this is an apple. I am hungry. this is a orage. this is a pen. I should study.I do not study.\\nthis is a computergame." // target
                    , new String[]{"this is an apple.","an","apple"} // expected_regex
                    , new String[][]{{"this is an apple.","an","apple"},{"this is a computergame.","a","computergame"}} // expected_data
                    ));
        // DOTALL
        System.out.println(testAll_OnceNewInstance(
                    "/this.(is an)/s" // regex
                    , "this\nis an apple." // target
                    , new String[]{"this\nis an","is an"} // expected_regex
                    , new String[][]{{"this\nis an","is an"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/(?s)this.(is an)/" // regex
                    , "this\nis an apple." // target
                    , new String[]{"this\nis an","is an"} // expected_regex
                    , new String[][]{{"this\nis an","is an"}} // expected_data
                    ));
        // Unixラインモード
        System.out.println(!testAll_OnceNewInstance( // falseとなるのが正しいので、!で反転させている
                    "/very$/gdm" // regex
                    , "very\r\ngood" // target // \r\nが改行として認識されないので、multilineモードでも$が適用されず、マッチしない
                    , new String[]{"very"} // expected_regex
                    , new String[][]{{"very"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/very$/gdm" // regex
                    , "very\ngood" // target
                    , new String[]{"very"} // expected_regex
                    , new String[][]{{"very"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/(?d)very$/gm" // regex
                    , "very\ngood" // target
                    , new String[]{"very"} // expected_regex
                    , new String[][]{{"very"}} // expected_data
                    ));
        // パターン内で空白とコメントを使用
        System.out.println(testAll_OnceNewInstance(
                    "/^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gmx" // regex
                    , "red\nblue\ngreen\ngrey\ngold\norange\nbrown" // target
                    , new String[]{"green","grey","gold"} // expected_regex
                    , new String[][]{{"green"},{"grey"},{"gold"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/(?x)^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gm" // regex
                    , "red\nblue\ngreen\ngrey\ngold\norange\nbrown" // target
                    , new String[]{"green","grey","gold"} // expected_regex
                    , new String[][]{{"green"},{"grey"},{"gold"}} // expected_data
                    ));
        // Unicodeに準拠した大文字と小文字を区別しない
        System.out.println(testAll_OnceNewInstance(
                    "/^ｇ\\S+$/gmui" // regex uオプションはiオプションと一緒に指定する必要がある
                    , "ｒＥｄ\nｂｌｕＥ\nＧＲＥＥＮ\nＧＲＥｙ\nｇｏｌｄ\nｏｒａＮｇＥ\nｂＲＯｗＮ" // target
                    , new String[]{"ＧＲＥＥＮ","ＧＲＥｙ","ｇｏｌｄ"} // expected_regex
                    , new String[][]{{"ＧＲＥＥＮ"},{"ＧＲＥｙ"},{"ｇｏｌｄ"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/(?u)(?i)^ｇ\\S+$/gm" // regex uオプションはiオプションと一緒に指定する必要がある
                    , "ｒＥｄ\nｂｌｕＥ\nＧＲＥＥＮ\nＧＲＥｙ\nｇｏｌｄ\nｏｒａＮｇＥ\nｂＲＯｗＮ" // target
                    , new String[]{"ＧＲＥＥＮ","ＧＲＥｙ","ｇｏｌｄ"} // expected_regex
                    , new String[][]{{"ＧＲＥＥＮ"},{"ＧＲＥｙ"},{"ｇｏｌｄ"}} // expected_data
                    ));
        // パターンのリテラル構文解析を有効にする
        System.out.println(!testAll_OnceNewInstance( // falseとなるのが正しいので、!で反転させている
                    "/^g\\S+$/gml" // regex
                    , "red\nblue\ngreen\ngrey\ngold\norange\nbrown" // target
                    , new String[]{"green","grey","gold"} // expected_regex
                    , new String[][]{{"green"},{"grey"},{"gold"}} // expected_data
                    ));
        System.out.println(testAll_OnceNewInstance(
                    "/^g\\S+$/glm" // regex
                    , "red\nblue\n^g\\S+$grey\ngold\n^g\\S+$\nbrown" // target
                    , new String[]{"^g\\S+$","^g\\S+$"} // expected_regex
                    , new String[][]{{"^g\\S+$"},{"^g\\S+$"}} // expected_data
                    ));

        System.out.println("-----------------------------------------------------------");
        // ゲッター、セッター、has系メソッド
        System.out.println("getter, setter, has*()");
        System.out.println(Regex.newInstance("/this [^.]+\\./","this is an apple. this is a pen.").getTarget().equals("this is an apple. this is a pen."));
        System.out.println(Regex.newInstance("/this [^.]+\\./","this is an apple. this is a pen.").getRegex().equals("this [^.]+\\."));
        System.out.println(Regex.newInstance("/this [^.]+\\./g","this is an apple. this is a pen.").hasOptionG());
        System.out.println(Regex.newInstance("/(tes)t/gi","testTestTESTtEst").hasOptionI());
        System.out.println(Regex.newInstance("/^this is (a|an) (\\S+)\\./m","this is an apple. I am hungry. this is a orage. this is a pen. I should study.I do not study.\\nthis is a computergame.").hasOptionM());
        System.out.println(Regex.newInstance("/this.(is an)/s","this\nis an apple.").hasOptionS());
        System.out.println(Regex.newInstance("/very$/gdm","very\ngood").hasOptionD());
        System.out.println(Regex.newInstance("/^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gmx","red\nblue\ngreen\ngrey\ngold\norange\nbrown").hasOptionX());
        System.out.println(Regex.newInstance("/^g\\S+$/gl","red\nblue\n^g\\S+$grey\ngold\n^g\\S+$\nbrown").hasOptionL());

        System.out.println("-----------------------------------------------------------");
        // error
        // 例外が出るのが正しいのでこのまま続行する
        System.out.println("例外テスト");
        try {
            System.out.println(Regex.newInstance("SampleSAMPLESIMPLE","sample/i").find(0).group());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Regex.newInstance("SampleSAMPLESIMPLE","/sample/i").find(2).group(0));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Regex.newInstance("SampleSAMPLESIMPLE").matchCount());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Regex.newInstance("/SampleSAMPLESIMPLE/").find(0).group(2));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Regex.newInstance("SampleSAMPLESIMPLE").match("/sample/gi").group(2));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------------------------------------------------");
        // Iterator
        System.out.println("iterator");
        // gオプション
        System.out.println(test_foreach(
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/sample/gi"),
                    new String[]{"Sample","SAMPLE","sample","SaMpLe"}
                    ));
        // gオプションなし
        System.out.println(test_foreach(
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/i"),
                    new String[]{"Sample","S","a","m","p","l","e"}
                    ));
        // Dataのイテレート
        System.out.println(test_foreach_nestClass(
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi").find(3),
                    new String[]{"SaMpLe","S","a","M","p","L","e"}
                    ));
        // 範囲外Dataのイテレート
        try {
        System.out.println(test_foreach_nestClass(
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi").find(4),
                    new String[]{}
                    ));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ここでは例外発生が正常");
            e.printStackTrace();
        }
        // ３つ目のグループをイテレート
        System.out.println(test_foreach_group(
                    3,
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi"),
                    new String[]{"m","M","m","M"}
                    ));
        // グループイテレートにgオプションの有無は関係ない
        System.out.println(test_foreach_group(
                    3,
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/i"),
                    new String[]{"m","M","m","M"}
                    ));
        // groupIterator(0)のイテレートはgオプションありと同じ結果
        System.out.println(test_foreach_group(
                    0,
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/sample/i"),
                    new String[]{"Sample","SAMPLE","sample","SaMpLe"}
                    ));
        // iteratorを取り出して直接使用(group)
        System.out.println(test_groupIterator(
                    1,
                    Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/s(amp)le/i"),
                    new String[]{"amp","AMP","amp","aMp"}
                    ));

        System.out.println("-----------------------------------------------------------");

        timer.Timer timer = new timer.Timer();
        timer.start("test");
        timer.end("test");
        timer.start("new");
        Regex.newInstance("SampleSAMPLESIMPLE","/sample/i").find(0).group();
        timer.end("new");
        Regex reg = Regex.newInstance("SampleSAMPLESIMPLE");
        timer.start("match");
        String res = reg.match("/sample/i").find(0).group();
        timer.end("match");
        timer.start("get");
        res = reg.find(0).group();
        timer.end("get");

    }

    private static boolean test_groupIterator(int index, Regex reg, String[] expected) {
        int counter = 0;
        for (Iterator<String> i = reg.groupIterator(index); i.hasNext(); counter++) {
            String result = i.next();
            if (!result.equals(expected[counter])) {
                return false;
            }
        }
        return true;
    }

    private static boolean test_foreach(Regex reg, String[] expected) {
        int counter = 0;
        for (String result : reg) {
            if (!result.equals(expected[counter])) {
                return false;
            }
            counter++;
        }
        if (counter == expected.length) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean test_foreach_group(int index, Regex reg, String[] expected) {
        int counter = 0;
        for (String result : reg.groupIterator(index)) {
            if (!result.equals(expected[counter])) {
                return false;
            }
            counter++;
        }
        if (counter == expected.length) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean test_foreach_nestClass(Regex.Data data, String[] expected) {
        int counter = 0;
        for (String result : data) {
            if (!result.equals(expected[counter])) {
                return false;
            }
            counter++;
        }
        if (counter == expected.length) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean testAll_Both_Regex(Regex reg,String[] expected_regex,String[][] expected_data) {
        if (!testAll_RegexGroup(reg,expected_regex)) {
            // System.out.println("false in testAll_Both_Regex");
            return false;
        }
        if (!testAll_DataGroup(reg,expected_data)) {
            // System.out.println("false in testAll_Both_Regex");
            return false;
        }
        return true;
    }
    private static boolean testAll_OnceNewInstance(String regex, String target, String[] expected_regex, String[][] expected_data) {
        if (!testAll_OnceNewInstance_RegexGroup(regex,target,expected_regex)) {
            // System.out.println("false in testAll_OnceNewInstance");
            return false;
        }
        if (!testAll_OnceNewInstance_DataGroup(regex, target, expected_data)) {
            // System.out.println("false in testAll_OnceNewInstance");
            return false;
        }
        return true;
    }
    private static boolean testAll_OnceNewInstance_RegexGroup(String regex, String target, String[] expected) {
        Regex reg = Regex.newInstance(target,regex);
        // System.out.println(reg);
        return testAll_RegexGroup(reg, expected);
    }
    private static boolean testAll_OnceNewInstance_DataGroup(String regex, String target, String[][] expected) {
        Regex reg = Regex.newInstance(target,regex);
        if (!testAll_DataGroup(reg,expected)) {
            // System.out.println("false in testAll_OnceNewInstance_DataGroup");
            return false;
        }
        Regex reg2 = Regex.newInstance(regex,target);
        if (!testAll_DataGroup(reg2,expected)) {
            // System.out.println("false in testAll_OnceNewInstance_DataGroup");
            return false;
        }
        return true;
    }
    private static boolean testAll_RegexGroup_recycleTarget(String target, String[] regexes, String[][] expected) {
        Regex regex = Regex.newInstance(target);
        for (int i = 0; i < regexes.length; i++) {
            regex.match(regexes[i]); // 正規表現の変更
            if(!testAll_RegexGroup(regex,expected[i])){
                // System.out.println("false in testAll_RegexGroup_recycleTarget");
                return false;
            }
        }
        return true;
    }
    private static boolean testAll_RegexGroup(Regex reg, String[] expected) {
        if (!reg.test()) {
            return false;
        }
        if (reg.groupCount() != expected.length) {
            return false;
        }
        for (int i = 0,len = reg.groupCount(); i < len; i++) {
            if (!reg.group(i).equals(expected[i])) {
                // System.out.println("false in testAll_RegexGroup");
                return false;
            }
        }
        return true;
    }

    private static boolean testAll_DataGroup_recycleRegex(String regex, String[] targets, String[][][] expected) {
        Regex reg = Regex.newInstance(regex);
        for (int i = 0,len = targets.length; i < len; i++) {
            reg.match(targets[i]); // 調査対象文字列の変更
            if (!testAll_DataGroup(reg, expected[i])) {
                // System.out.println("false in testAll_DataGroup_recycleRegex");
                return false;
            }
        }
        if (!reg.test()) {
            return false;
        }
        return true;
    }
    private static boolean testAll_DataGroup(Regex reg, String[][] expected) {
        for (int i = 0,len = reg.matchCount(); i < len; i++) {
            if (!testAll_DataGroup(reg.find(i),expected[i])) {
                // System.out.println("false in testAll_DataGroup");
                return false;
            }
        }
        return true;
    }

    private static boolean testAll_DataGroup(Regex.Data data, String[] expected) {
        if (data.size() != expected.length) {
            return false;
        }
        for (int i = 0,len = data.size(); i < len; i++) {
            if (!data.group(i).equals(expected[i])) {
                // System.out.println("false in testAll_DataGroup2");
                // System.out.println("expected:"+ formatArray(expected) + ",result:"+ data.toString());
                return false;
            }
        }
        return true;
    }

    private static String formatArray(String[] array) {
        StringBuilder sb = new StringBuilder("[");

        for (int i=0,len=array.length; i<len; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("\"");
            sb.append(array[i]);
            sb.append("\"");
        }
        sb.append("]");

        return sb.toString();
    }
    */
}
