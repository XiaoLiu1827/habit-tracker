var modal = document.getElementById("myModal");
const startButton = document.getElementById("startButton");
var closeButton = document.getElementsByClassName("close")[0];
let activeHabits = [];
let currentHabitIndex = 0; // 現在の習慣のインデックス
const nextButton = document.getElementById("nextButton");
let reviewResults = []; // 各習慣の振り返りデータをここに蓄積
let questionMap = new Map(); // カテゴリ別に質問リストを格納

//**イベント */

// ページ読み込み時
window.addEventListener('DOMContentLoaded', function() {
	// APIから習慣一覧を取得
	fetchActiveHabits();
	//質問リストを取得
	fetchQuestions();
});

// スタートボタンをクリックした時にモーダルを表示
startButton.onclick = function() {
	if (activeHabits.length === 0) {
		alert('習慣が登録されていません');
		return;
	}

	currentHabitIndex = 0; // 最初の習慣からスタート
	//習慣名をモーダルに表示する
	showHabitOnModal(currentHabitIndex);
	handleModalFlow();
}

async function handleModalFlow() {
	modal.style.display = "block";
	await waitForCloseOrWindowClicked();
	modal.style.display = "none";

}

function waitForCloseOrWindowClicked() {
	return new Promise(resolve => {
		closeButton.addEventListener('click', () => {
			resolve();
		}, { once: true });
	});
}

// モーダルの×ボタンをクリックした時にモーダルを閉じる
closeButton.onclick = function() {
	modal.style.display = "none";
}

// モーダルの外をクリックした時にモーダルを閉じる
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}

//  成功/失敗選択時に「次へ」ボタンと質問ブロックを表示
document.querySelectorAll('input[name="result"]').forEach(radio => {
	radio.addEventListener('change', function() {
		//document.getElementById("questionBlock").style.display = "block";
		displayQuestions();
		document.getElementById("navigationButtons").style.display = "block";
	});
});

//「次へ」ボタン押下時の処理
nextButton.addEventListener('click', function() {
	//現在の習慣に対する入力を取得
	const result = document.querySelector('input[name="result"]:checked')?.value;
	const selectedChoices = Array.from(document.querySelectorAll('#questionBlock input[type="checkbox"]:checked'))
		.map(cb => parseInt(cb.value));

	if (!result) {
		alert("成功か失敗を選んでください");
		return;
	}

	// 現在の習慣を特定
	const currentHabit = activeHabits[currentHabitIndex];

	// データを保存
	reviewResults.push({
		habitId: currentHabit.id,
		success: result === 'success',
		answerIds: selectedChoices
	});
	currentHabitIndex++;

	//習慣が残っている場合は次の習慣へ
	if (currentHabitIndex < activeHabits.length) {
		resetModalState();
		showHabitOnModal(currentHabitIndex);
	} else {
		// 最後の習慣まで終わったら送信モードに切り替え
		document.getElementById("modalHabitLabel").textContent = "すべての振り返りが完了しました！";
		document.querySelector(".result-options").style.display = "none";
		document.getElementById("questionBlock").style.display = "none";
		document.getElementById("navigationButtons").innerHTML = '<button id="submitButton">送信する</button>';

		// 送信ボタンのイベント（この時点では仮のconsole出力）
		document.getElementById("submitButton").addEventListener('click', () => {
			console.log("送信するデータ:", reviewResults);

			// TODO: ここでfetch()など使ってAPIへ送信できるようにする
			alert("振り返りを送信しました！");
			modal.style.display = "none";

			// 初期化して再スタートできるようにするなら↓
			reviewResults = [];
			currentHabitIndex = 0;
		});
	}
})

//**関数 */

//カテゴリに応じた質問を押下されたラジオボタンの下部に表示する
function displayQuestions() {
	// habitTypeは "CONTINUE" または "QUIT"
	const habiType = activeHabits[currentHabitIndex].habitType;

	//カテゴリを取得
	const categoryKey = getCategoryKey(habiType);
}

//どのカテゴリの質問を出すか決定する
function getCategoryKey(habitType, isSuccess) {
	// habitTypeは "CONTINUE" または "QUIT"
	const lowerType = habitType.toLowerCase(); // "continue" or "quit"
	const result = isSuccess ? "success" : "failure";
	return `${lowerType}-${result}`; // 例："continue-success"
}

//質問リストを取得して保持する
async function fetchQuestions() {
	try {
		const res = await fetch('/api/review/questions');
		const questions = await res.json();
		categorizeQuestions(questions);
	} catch (error) {
		console.error('質問取得失敗:', error);
	}
}

//質問をカテゴリごとに分類
function categorizeQuestions(questions) {
	questions.forEach(q => {
		//カテゴリが質問リストにまだない場合追加する
		if (!questionMap.has(q.categoryKey)) {
			questionMap.set(q.categoryKey, []);
		}
		questionMap.get(q.categoryKey).push(q); // choices込みでそのまま格納
	});
}

// モーダル表示を初期化する関数
function resetModalState() {
	// ラジオボタンのチェック解除
	document.querySelectorAll('input[name="result"]').forEach(radio => radio.checked = false);
	// 質問・ナビボタン非表示
	document.getElementById("questionBlock").style.display = "none";
	document.getElementById("navigationButtons").style.display = "none";
}


// モーダルに習慣名を表示する関数
function showHabitOnModal(index) {
	const habit = activeHabits[index];
	console.log(habit);
	document.getElementById("modalHabitLabel").textContent = habit.label;
}

// APIからユーザーのアクティブな習慣を取得する関数
async function fetchActiveHabits() {
	// ユーザーIDを取得（実際の実装に合わせて調整）
	const userId = 1; // 例として固定値を使用

	// APIリクエスト
	try {
		const response = await fetch(`/api/habits?userId=${userId}`);
		const habits = await response.json();

		//グローバル変数に格納
		activeHabits = habits;
		// ホーム画面に習慣のラベルと説明を表示
		displayHabitsOnHome(habits);

	} catch (error) {
		console.error('習慣データの取得に失敗:', error);
	};
}

// ホーム画面に習慣を表示する関数
function displayHabitsOnHome(habits) {
	const container = document.getElementById('homeHabitsContainer');
	container.innerHTML = ''; // 一旦クリア

	habits.forEach(habit => {
		const habitDiv = document.createElement('div');
		habitDiv.className = 'habit-item';

		const title = document.createElement('h3');
		title.textContent = habit.label;

		const desc = document.createElement('p');
		desc.textContent = habit.description;

		habitDiv.appendChild(title);
		habitDiv.appendChild(desc);
		container.appendChild(habitDiv);
	});
}
