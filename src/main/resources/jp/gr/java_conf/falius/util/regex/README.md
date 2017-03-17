# RegEx
正規表現を扱いやすくするクラスです

javaの正規表現はPatternクラスとMatcherクラスという２つのクラスを使用する必要がありますが、１つのクラスで対応できるようにしました

newInstance()とmatch()を使用して正規表現とターゲット文字列を渡すことでマッチングを行います

__※より詳しい内容はdocディレクトリ内にあるindex.htmlよりjavadocをご覧ください__

## Usage
### 主なメソッド
_newInstance(String regexOrTarget)_: 正規表現か対象文字列を渡してインスタンスを生成する

_match(String regexOrTarget)_: 正規表現か対象文字列(newInstance()で渡していない方)を渡してマッチングを行う。戻り値は自身のインスタンス

_find(int index)_:０から始まるインデックスを引数に指定して、マッチ箇所を選択する。戻り値はマッチ箇所を扱うRegEx.Dataインスタンス

_group()_: group(0)と同じ

_group(int index)_: (RegExインスタンスのメソッド)正規表現にgオプションを指定していれば、0から始まるインデックスで指定されたマッチ箇所の文字列を返す(find(index).group()と同じ)。gオプションを指定していなければ、最初にマッチした箇所のgroup()メソッドを実行してインデックスで指定されたグループ文字列を返す(find(0).group(index)と同じ)

_group(int index)_: (RegEx.Dataインスタンスのメソッド)１から始まるインデックスを引数に指定して、マッチ箇所のうちグループ化された文字列を取得する。0を渡すとマッチ箇所の文字列全体を取得

_test()_: マッチしたのか否かを真偽値で返す

その他のメソッドはjavadoc参照

### 基本
find(int)でマッチ箇所を選択し、メソッドチェーンでgroup(int)によって該当文字列を取り出すことができます

正規表現はスラッシュで囲んだ文字列によって表されます
例:"/a (pen)\\./"

_引数の正規表現とターゲット文字列は順不同で渡すことができます_
`RegEx.newInstance("this is a pen.").match("/a (pen)\\./")`
と
`RegEx.newInstance("/a (pen)\\./").match("this is a pen.")`
に違いはありません

find(int)を使わずRegExオブジェクトが直接持つgroup(int)の戻り値は、正規表現を/regex/gの形にしたかどうかで変化します
また、小文字と大文字を区別しないオプションをつけるには、/regex/iと最後のスラッシュの後にiをつけてください
オプションは複数指定することもできます

### 基本的な使用の流れ: マッチ箇所の文字列を取得する
`RegEx regex = RegEx.newInstance("target string").match("/(\\S+)/");`
`String result = regex.find(0).group(); // →　"target"を取得`
`String result2 = regex.find(1).group() //　→　"string"を取得`

### 基本的な使用の流れ２: マッチ箇所のグループ化された文字列を取得する
`RegEx regex = RegEx.newInstance("target string").match("/targe (\\S+)/");`
`String result = regex.find(0).group(1); // →　"string"を取得`

### 基本的な使用の流れ３: 一つの正規表現に複数の文字列をマッチングしていく(逆も可能)
`RegEx reg = RegEx.newInstance("/sample/i"); // 大文字小文字を区別しないオプションをつけている`
`boolean bool = reg.match("sample").test(); // true`
`boolean bool2 = reg.match("simple").test(); // false`
`boolean bool3 = reg.match("Sample").test(); // true`
`boolean bool4 = reg.match("SAMPLE").test(); // true`
`boolean bool5 = reg.match("Simple").test(); // false`

この場合でも、内部ではPatternクラスのインスタンスとMatcherクラスのインスタンスを使いまわしているので、match()を使用するたびにオブジェクトが生成されるということはありません
