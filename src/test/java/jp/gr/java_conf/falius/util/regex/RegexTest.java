package jp.gr.java_conf.falius.util.regex;

public class RegexTest {

//    @Test
//    public void oneTarget_multiRegex_directGroupTest() {
//        // Regexインスタンスに直接group()を使用した場合
//        // 調査対象文字列は一度だけ渡し、正規表現を複数回渡すことで調査対象文字列を使いまわす
//        String target = "this is an apple. I am hungry. this is a pen. I should study.";
//        String[] regexes = { "/this is (a|an) \\S+\\./", "/[^\\.]+\\./g", "/i[^y.]*y/gi" };
//        String[][] expecteds = {
//                { "this is an apple.", "an" },
//                { "this is an apple.", " I am hungry.", " this is a pen.", " I should study." },
//                { "I am hungry", "I should study" }
//        };
//
//        Regex regex = Regex.newInstance(target);
//        int cnt = 0;
//        for (String strRegex : regexes) {
//            regex.match(strRegex);
//            String[] expected = expecteds[cnt++];
//
//            assertTrue(regex.test());
//            assertThat(regex.groupCount(), is(expected.length));
//
//            for (int i = 0, len = regex.groupCount(); i < len; i++) {
//                assertThat(regex.group(i), is(expected[i]));
//            }
//        }
//    }
//
//    @Test
//    public void findTest() {
//        // find()してからのgroup()
//        // 正規表現は一度だけ渡し、調査対象文字列は複数回渡すことで正規表現を使いまわす
//        String regex = "/this is (a|an) (\\S+)\\./";
//        String[] targets = { "this is an apple. this is a pen.", "this is a cup. this is a smartphone." };
//        String[][][] expected = {
//                {
//                        { "this is an apple.", "an", "apple" },
//                        { "this is a pen.", "a", "pen" }
//                },
//                {
//                        { "this is a cup.", "a", "cup" },
//                        { "this is a smartphone.", "a", "smartphone" }
//                }
//        };
//
//        testAll_DataGroup_recycleRegex(regex, targets, expected);
//    }
//
//    private void testAll_DataGroup_recycleRegex(
//            String regex, String[] targets, String[][][] expected) {
//        Regex reg = Regex.newInstance(regex);
//        for (int i = 0; i < targets.length; i++) {
//            reg.match(targets[i]); // 調査対象文字列の変更
//            testAll_DataGroup(reg, expected[i]);
//        }
//
//        assertTrue(reg.test());
//    }
//
//    private void testAll_DataGroup(Regex reg, String[][] expected) {
//        IntStream.range(0, reg.matchCount())
//                .forEach(i -> testAll_DataGroup(reg.find(i), expected[i]));
//        ;
//    }
//
//    private void testAll_DataGroup(Regex.Data data, String[] expected) {
//        assertThat(data.size(), is(expected.length));
//        IntStream.range(0, data.size())
//                .forEach(i -> assertThat(data.group(i), is(expected[i])));
//    }
//
//    @Test
//    public void onceNewInstanceTest() {
//        // newInstanceに正規表現と調査対象文字列を同時に渡す
//        // また、正規表現と調査対象文字列が順不同であることも同時に確認
//        String regex = "/this is (a|an) (\\S+)\\./";
//        String target = "this is an apple. I am hungry. this is a pen. I should study.";
//        String[][] expected = {
//                { "this is an apple.", "an", "apple" },
//                { "this is a pen.", "a", "pen" }
//        };
//        testAll_OnceNewInstance_DataGroup(regex, target, expected);
//    }
//
//    private void testAll_OnceNewInstance_DataGroup(String regex, String target, String[][] expected) {
//        Regex reg = Regex.newInstance(target, regex);
//        testAll_DataGroup(reg, expected);
//        Regex reg2 = Regex.newInstance(regex, target);
//        testAll_DataGroup(reg2, expected);
//    }
//
//    @Test
//    public void allMatchTest() {
//        // group(),find().group()すべてを確認
//        Regex reg = Regex.newInstance("/'(\\S+) (green)'/g")
//                .match("there are many colors. I like 'lite green','yellow green' and 'dark green'.");
//        String[] expected_regex = { "'lite green'", "'yellow green'", "'dark green'" };
//        String[][] expected_data = {
//                { "'lite green'", "lite", "green" },
//                { "'yellow green'", "yellow", "green" },
//                { "'dark green'", "dark", "green" }
//        };
//        testAll_Both_Regex(reg, expected_regex, expected_data);
//    }
//
//    private void testAll_Both_Regex(Regex reg, String[] expected_regex, String[][] expected_data) {
//        testAll_RegexGroup(reg, expected_regex);
//        testAll_DataGroup(reg, expected_data);
//    }
//
//    private void testAll_RegexGroup(Regex reg, String[] expected) {
//        assertTrue(reg.test());
//        assertThat(reg.groupCount(), is(expected.length));
//
//        IntStream.range(0, reg.groupCount())
//                .forEach(i -> assertThat(reg.group(i), is(expected[i])));
//    }
//
//    @Test
//    public void methodChainTest() {
//        // メソッドチェーン
//        // 1:newInstance()に正規表現を渡してインスタンスを作成
//        // 2:match()にターゲット文字列を渡してマッチング
//        // 3:find(2)でインデックスが２(０から始まるので３番目)の部分文字列を扱うDataオブジェクトを取得
//        // 4:group(1)で１つ目のグループの文字列を取得
//        // 結果として、"dark"を取得する
//        assertThat(Regex.newInstance("/'(\\S+) (green)'/g")
//                .match(
//                        "there are many colors. example,'lite green','orange red',"
//                                + "'yellow green','sky blue' and 'dark green'.")
//                .find(2).group(1), is("dark"));
//    }
//
//    @Test
//    public void testMethodTest() {
//        assertTrue(Regex.newInstance(
//                "there are many greens. I like 'lite green','yellow green' and 'dark green'.", "/yellow green/")
//                .test());
//        assertTrue(Regex.newInstance(
//                "there are many greens. I like 'lite green','yellow green' and 'dark green'.")
//                .match("/\\S+ green'/").find(1)
//                .test()); // ２つ目のマッチ文字列が存在する
//        assertTrue(Regex.test(
//                "there are many greens. I like 'lite green','yellow green' and 'dark green'.", "/yellow green/"));
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void testFailedTest() {
//        Regex.newInstance(
//                "there are many greens. I like 'lite green','yellow green' and 'dark green'.")
//                .match("/\\S+ green'/").find(3) // ４つ目のマッチ文字列が存在しない
//                .test();
//    }
//
//    @Test
//    public void matchesTest() {
//        assertTrue(Regex.newInstance("Test", "/t.st/i").matches());
//        assertFalse(Regex.newInstance("tTest", "/t.st/i").matches());
//        assertTrue(Regex.newInstance("tTest", "/t.st/i").test()); // 同様の組み合わせでも、test()ならtrue
//        assertTrue(Regex.matches("Test", "/(?i)t.st/"));
//    }
//
//    @Test
//    public void giOptionTest() {
//        // gi
//        String regex = "/(tes)t/gi";
//        String target = "testTestTESTtEst";
//        String[] expected_regex = { "test", "Test", "TEST", "tEst" };
//        String[][] expected_data = { { "test", "tes" }, { "Test", "Tes" }, { "TEST", "TES" }, { "tEst", "tEs" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    private void testAll_OnceNewInstance(String regex, String target, String[] expected_regex,
//            String[][] expected_data) {
//        testAll_RegexGroup(Regex.newInstance(target, regex), expected_regex);
//        testAll_OnceNewInstance_DataGroup(regex, target, expected_data);
//    }
//
//    @Test
//    public void iInnerFlagOptionTest() {
//        // inner flag
//        String regex = "/(?i)(tes)t/g";
//        String target = "testTestTESTtEst";
//        String[] expected_regex = { "test", "Test", "TEST", "tEst" };
//        String[][] expected_data = { { "test", "tes" }, { "Test", "Tes" }, { "TEST", "TES" }, { "tEst", "tEs" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void multilineOptionTest() {
//        String regex = "/^this is (a|an) (\\S+)\\./m";
//        String target = "this is an apple. I am hungry. this is a orage. this is a pen. I should study.I do not study.\\nthis is a computergame.";
//        String[] expected_regex = { "this is an apple.", "an", "apple" };
//        String[][] expected_data = {
//                { "this is an apple.", "an", "apple" }, { "this is a computergame.", "a", "computergame" }
//        };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void multilineInnerFlagOptionTest() {
//        String regex = "/(?m)^this is (a|an) (\\S+)\\./";
//        String target = "this is an apple. I am hungry. this is a orage. this is a pen."
//                + " I should study.I do not study.\\nthis is a computergame.";
//        String[] expected_regex = { "this is an apple.", "an", "apple" };
//        String[][] expected_data = {
//                { "this is an apple.", "an", "apple" }, { "this is a computergame.", "a", "computergame" }
//        };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void dotallOptionTest() {
//        String regex = "/this.(is an)/s";
//        String target = "this\nis an apple.";
//        String[] expected_regex = { "this\nis an", "is an" };
//        String[][] expected_data = { { "this\nis an", "is an" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void dotallInnerFlagOptionTest() {
//        String regex = "/(?s)this.(is an)/";
//        String target = "this\nis an apple.";
//        String[] expected_regex = { "this\nis an", "is an" };
//        String[][] expected_data = { { "this\nis an", "is an" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void unixlineOptionTest() {
//        // Unixラインモード
//        String regex = "/very$/gdm";
//        String target = "very\r\ngood"; // \r\nが改行として認識されないので、multilineモードでも$が適用されず、マッチしない
//        Regex reg = Regex.newInstance(regex, target);
//        assertFalse(reg.test());
//        assertThat(reg.groupCount(), is(0));
//        testAll_OnceNewInstance_DataGroup(regex, target, new String[][] { {} });
//
//        testAll_OnceNewInstance(
//                "/very$/gdm" // regex
//                , "very\ngood" // target
//                , new String[] { "very" } // expected_regex
//                , new String[][] { { "very" } } // expected_data
//        );
//        testAll_OnceNewInstance(
//                "/(?d)very$/gm" // regex
//                , "very\ngood" // target
//                , new String[] { "very" } // expected_regex
//                , new String[][] { { "very" } } // expected_data
//        );
//    }
//
//    @Test
//    public void commentsOptionTest() {
//        // パターン内で空白とコメントを使用
//        String regex = "/^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gmx";
//        String target = "red\nblue\ngreen\ngrey\ngold\norange\nbrown";
//        String[] expected_regex = { "green", "grey", "gold" };
//        String[][] expected_data = { { "green" }, { "grey" }, { "gold" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void commentsInnerFlagOptionTest() {
//        String regex = "/(?x)^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gm";
//        String target = "red\nblue\ngreen\ngrey\ngold\norange\nbrown";
//        String[] expected_regex = { "green", "grey", "gold" };
//        String[][] expected_data = { { "green" }, { "grey" }, { "gold" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void unicodecaseOptionTest() {
//        // Unicodeに準拠した大文字と小文字を区別しない
//        String regex = "/^ｇ\\S+$/gmui"; // regex uオプションはiオプションと一緒に指定する必要がある
//        String target = "ｒＥｄ\nｂｌｕＥ\nＧＲＥＥＮ\nＧＲＥｙ\nｇｏｌｄ\nｏｒａＮｇＥ\nｂＲＯｗＮ";
//        String[] expected_regex = { "ＧＲＥＥＮ", "ＧＲＥｙ", "ｇｏｌｄ" };
//        String[][] expected_data = { { "ＧＲＥＥＮ" }, { "ＧＲＥｙ" }, { "ｇｏｌｄ" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void unicodecaseInnerFlagOptionTest() {
//        String regex = "/(?u)(?i)^ｇ\\S+$/gm"; // regex uオプションはiオプションと一緒に指定する必要がある
//        String target = "ｒＥｄ\nｂｌｕＥ\nＧＲＥＥＮ\nＧＲＥｙ\nｇｏｌｄ\nｏｒａＮｇＥ\nｂＲＯｗＮ";
//        String[] expected_regex = { "ＧＲＥＥＮ", "ＧＲＥｙ", "ｇｏｌｄ" };
//        String[][] expected_data = { { "ＧＲＥＥＮ" }, { "ＧＲＥｙ" }, { "ｇｏｌｄ" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void literalOptionTest() {
//        // パターンのリテラル構文解析を有効にする
//        String regex = "/^g\\S+$/gml";
//        String target = "red\nblue\ngreen\ngrey\ngold\norange\nbrown";
//        Regex reg = Regex.newInstance(regex, target);
//        assertFalse(reg.test());
//        assertThat(reg.groupCount(), is(0));
//    }
//
//    @Test
//    public void literalInnerFlagOptionTest() {
//        String regex = "/^g\\S+$/glm";
//        String target = "red\nblue\n^g\\S+$grey\ngold\n^g\\S+$\nbrown";
//        String[] expected_regex = { "^g\\S+$", "^g\\S+$" };
//        String[][] expected_data = { { "^g\\S+$" }, { "^g\\S+$" } };
//        testAll_OnceNewInstance(regex, target, expected_regex, expected_data);
//    }
//
//    @Test
//    public void accessorTest() {
//        // ゲッター、セッター
//        assertThat(Regex.newInstance("/this [^.]+\\./", "this is an apple. this is a pen.").getTarget(),
//                is("this is an apple. this is a pen."));
//        assertThat(Regex.newInstance("/this [^.]+\\./", "this is an apple. this is a pen.").getRegex(),
//                is("this [^.]+\\."));
//    }
//
//    @Test
//    public void hasMechodTest() {
//        // has系メソッド
//        assertTrue(Regex.newInstance("/this [^.]+\\./g", "this is an apple. this is a pen.").hasOptionG());
//        assertTrue(Regex.newInstance("/(tes)t/gi", "testTestTESTtEst").hasOptionI());
//        assertTrue(Regex
//                .newInstance("/^this is (a|an) (\\S+)\\./m",
//                        "this is an apple. I am hungry. this is a orage. this is a pen. I should study.I do not study.\\nthis is a computergame.")
//                .hasOptionM());
//        assertTrue(Regex.newInstance("/this.(is an)/s", "this\nis an apple.").hasOptionS());
//        assertTrue(Regex.newInstance("/very$/gdm", "very\ngood").hasOptionD());
//        assertTrue(Regex
//                .newInstance("/^#行頭から始まる\ng\\S+ #gから始まる単語\n$#ここが行末/gmx", "red\nblue\ngreen\ngrey\ngold\norange\nbrown")
//                .hasOptionX());
//        assertTrue(Regex.newInstance("/^g\\S+$/gl", "red\nblue\n^g\\S+$grey\ngold\n^g\\S+$\nbrown").hasOptionL());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void argFailedTest() {
//        Regex.newInstance("SampleSAMPLESIMPLE", "sample/i").find(0).group();
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void indexFailedTest() {
//        Regex.newInstance("SampleSAMPLESIMPLE", "/sample/i").find(2).group(0);
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void indexFailedTest2() {
//        Regex.newInstance("SampleSAMPLESIMPLE").match("/sample/gi").group(2);
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void stateFailedTest() {
//        Regex.newInstance("SampleSAMPLESIMPLE").matchCount();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void stateFailedTest2() {
//        Regex.newInstance("/SampleSAMPLESIMPLE/").find(0).group(2);
//    }
//
//    @Test
//    public void iteratorTest() {
//        // gオプション
//        test_foreach(
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/sample/gi"),
//                new String[] { "Sample", "SAMPLE", "sample", "SaMpLe" });
//        // gオプションなし
//        test_foreach(
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/i"),
//                new String[] { "Sample", "S", "a", "m", "p", "l", "e" });
//        // Dataのイテレート
//        test_foreach_nestClass(
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi").find(3),
//                new String[] { "SaMpLe", "S", "a", "M", "p", "L", "e" });
//        // ３つ目のグループをイテレート
//        test_foreach_group(
//                3,
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi"),
//                new String[] { "m", "M", "m", "M" });
//        // グループイテレートにgオプションの有無は関係ない
//        test_foreach_group(
//                3,
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/i"),
//                new String[] { "m", "M", "m", "M" });
//        // groupIterator(0)のイテレートはgオプションありと同じ結果
//        test_foreach_group(
//                0,
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/sample/i"),
//                new String[] { "Sample", "SAMPLE", "sample", "SaMpLe" });
//        // iteratorを取り出して直接使用(group)
//        test_groupIterator(
//                1,
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/s(amp)le/i"),
//                new String[] { "amp", "AMP", "amp", "aMp" });
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void iteratorFailedTest() {
//        // 範囲外Dataのイテレート
//        test_foreach_nestClass(
//                Regex.newInstance("SampleSAMPLEsampleSaMpLe").match("/(s)(a)(m)(p)(l)(e)/gi").find(4),
//                new String[] {});
//    }
//
//    private void test_groupIterator(int index, Regex reg, String[] expected) {
//        int counter = 0;
//        for (Iterator<String> i = reg.groupIterator(index); i.hasNext(); counter++) {
//            String result = i.next();
//            assertThat(result, is(expected[counter]));
//        }
//    }
//
//    private void test_foreach(Regex reg, String[] expected) {
//        int counter = 0;
//        for (String result : reg) {
//            assertThat(result, is(expected[counter]));
//            counter++;
//        }
//        assertThat(counter, is(expected.length));
//    }
//
//    private void test_foreach_group(int index, Regex reg, String[] expected) {
//        int counter = 0;
//        for (String result : reg.groupIterator(index)) {
//            assertThat(result, is(expected[counter]));
//            counter++;
//        }
//        assertThat(counter, is(expected.length));
//    }
//
//    private void test_foreach_nestClass(Regex.Data data, String[] expected) {
//        int counter = 0;
//        for (String result : data) {
//            assertThat(result, is(expected[counter]));
//            counter++;
//        }
//        assertThat(counter, is(expected.length));
//    }
}