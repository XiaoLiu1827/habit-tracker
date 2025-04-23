#DTO使用目的
・EntityはDB向けの情報のため、APIにとって分かりやすい形式に変換する。
・EntityとAPIレスポンス疎結合化を図る。

##QuestionWithChoicesDto
・QuestionWithChoicesDto
質問と選択肢をまとめた構造にすることでクライアントで扱いやすくする
・ChoiceDto
選択肢の中身をラベルだけ抽出（関連Entityごとのネスト排除）
・.from(...)
EntityからDTOへ変換する処理を当メソッドで集約する
