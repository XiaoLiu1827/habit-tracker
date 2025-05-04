var modal = document.getElementById("myModal");
const startButton = document.getElementById("startButton");
const closeButton = document.querySelector(".close");
let activeHabits = [];
let currentHabitIndex = 0; // 現在の習慣のインデックス
const nextHabitButton = document.getElementById("nextHabitButton");
let reviewResults = []; // 各習慣の振り返りデータをここに蓄積
let questionStoreMap = new Map(); // カテゴリ別に質問リストを格納

//**イベント */

// ページ読み込み時
window.addEventListener('DOMContentLoaded', function() {
	// APIから習慣一覧を取得
	fetchActiveHabits();
	//質問リストを取得
	fetchQuestions();
});


closeButton.addEventListener("click", () => {
	const shouldClose = confirm("振り返りを中断しますか？");
	if (shouldClose) {
		closeModalAndResetState();
	}
});

// スタートボタンをクリックした時にモーダルを表示
startButton.onclick = function() {
	if (activeHabits.length === 0) {
		alert('習慣が登録されていません');
		return;
	}

	currentHabitIndex = 0; // 最初の習慣からスタート
	//習慣名をモーダルに表示する
	handleModalFlow();
}

//**関数 */

//質問リストを取得して保持する
async function fetchQuestions() {
	try {
		const res = await fetch('/api/review/questions');

		const questions = await res.json();
		console.log('questionsの中身:', questions); // 👈 ここで構造を確認！

		storeQuestionsByCategory(questions);
	} catch (error) {
		console.error('質問取得失敗:', error);
	}
}

//モーダル上での振り返りフロー全体を管理
//ユーザの操作（ラジオ選択→質問回答→次へ）を、習慣ごとに繰り返す
//ユーザ操作及び内部の状態変化を一連の流れとして明示する
//全習慣の入力が完了したら送信画面へ移行
async function handleModalFlow() {
	modal.style.display = "block";
	while (currentHabitIndex < activeHabits.length) {
		resetModalState();

		showHabitOnModal(currentHabitIndex);

		const currentHabitType = activeHabits[currentHabitIndex].type; // CONTINUE or QUIT

		// 成功／失敗のラジオ選択に応じて質問を動的に表示するリスナーを設定
		setupResultChangeListener(currentHabitType);

		document.getElementById("navigationButtons").style.display = "block";

		// 「次へ」が押されるのを待つ → 入力内容を保存して次の習慣へ
		await waitForNextButtonClick();
		recordUserReview();
		currentHabitIndex++;
	}

	finishReviewFlow();
}

////閉じるボタンかモーダル外枠のクリックを待機
//function waitForClickCloseButtonOrOutSide() {
//	return new Promise(resolve => {
//		function handleClose() {
//			// どちらか押されたらresolve
//			resolve();
//			// イベントリスナーを外しておく（メモリリーク防止）
//			modal.removeEventListener('click', outsideClickListener);
//			closeButton.removeEventListener('click', closeClickListener);
//		}
//
//		function outsideClickListener(event) {
//			//event.target=クリックされたピンポイントの要素
//			//外枠押下時のみ閉じ、モーダルの中を押下時は閉じない
//			if (event.target === modal) {
//				handleClose();
//			}
//		}
//
//		function closeClickListener() {
//			handleClose();
//		}
//
//		//<div id="modal">の外枠＋中の要素全部がイベント対象
//		//※子要素で発生したイベントは親要素に伝わるため
//		modal.addEventListener('click', outsideClickListener);
//		closeButton.addEventListener('click', closeClickListener);
//	});
//}

// モーダルに習慣名を表示する関数
function showHabitOnModal(index) {
	const habit = activeHabits[index];
	console.log(habit);
	document.getElementById("modalHabitLabel").textContent = habit.label;
}

// ラジオボタンに常時イベントリスナーを設定
//習慣タイプとラジオボタンの結果に応じて質問を切り替え表示する
function setupResultChangeListener(currentHabitType) {
	const radios = document.querySelectorAll('input[name="result"]');

	radios.forEach(radio => {
		radio.addEventListener('change', event => {
			const selectedResult = event.target.value; // "success" or "failure"
			const categoryKey = getCategoryKey(currentHabitType, selectedResult === 'success');

			renderQuestionsForSelectedCategory(categoryKey);
			document.getElementById("questionBlock").style.display = "block";
		});
	});
}

//質問をカテゴリごとに分類
function storeQuestionsByCategory(questions) {
	//連想配列（object）を2重配列（[キー, 値] の配列の配列）に変換してから格納する
	Object.entries(questions).forEach(([categoryKey, questionList]) => {
		questionStoreMap.set(categoryKey, questionList);
	});
}

//// 成功／失敗のラジオボタンが選択されるのを待機し、その値を返す
//function waitForUserResultSelection() {
//	return new Promise(resolve => {
//		const radios = document.querySelectorAll('input[name="result"]');
//
//		function handler(event) {
//			radios.forEach(radio => {
//				//チェックするごとに毎回イベントリスナーを外す
//				//ラジオボタンを選び直せるよう、once:trueは設定しない
//				//				radio.removeEventListener('change', handler);
//			});
//			resolve(event.target.value);
//		}
//
//		radios.forEach(radio => {
//			radio.addEventListener('change', handler);
//		});
//	});
//}

////カテゴリに応じた質問を押下されたラジオボタンの下部に表示する
//function displayQuestions() {
//	// habitTypeは "CONTINUE" または "QUIT"
//	const habiType = activeHabits[currentHabitIndex].habitType;
//
//	//カテゴリを取得 isSuccessはラジオボタン押下時に取得する(todo)
//	const categoryKey = getCategoryKey(habiType, isSuccess);
//	renderQuestions(categoryKey);
//}

// 習慣タイプと成功/失敗結果から、該当する質問カテゴリキーを生成する
function getCategoryKey(habitType, isSuccess) {
	// habitTypeは "CONTINUE" または "QUIT"
	const UpperType = habitType.toUpperCase(); // "continue" or "quit"
	const result = isSuccess ? "SUCCESS" : "FAILURE";
	return `${UpperType}_${result}`; // 例："continue-success"
}

//カテゴリに応じて質問と選択肢を描画する
function renderQuestionsForSelectedCategory(categoryKey) {
	const container = document.getElementById("questionBlock");
	container.innerHTML = ""; // まずリセット

	const questionsList = questionStoreMap.get(categoryKey) || [];
	questionsList.forEach(question => {
		const questionDiv = document.createElement('div');
		const questionText = `<p>${question.text}</p>`;
		const choiceCheckboxes = question.choices.map(choice => `
			<label>
				<input type="checkbox" value="${choice.id}"> ${choice.label}
			</label><br>
		`).join("");

		questionDiv.innerHTML = questionText + choiceCheckboxes;
		container.appendChild(questionDiv);
	});
	document.getElementById("questionBlock").style.display = "block";

}

// 「次へ」ボタンがクリックされるのを待機する
function waitForNextButtonClick() {
	return new Promise(resolve => {
		function handler() {
			//ラジオボタンが選択されているか確認
			const result = document.querySelector('input[name="result"]:checked');
			if (!result) {
				alert("成功か失敗を選んでください");
				return; // 選択がないので進まない
			}
			
			nextHabitButton.removeEventListener('click', handler);
			resolve();
		}
		nextHabitButton.addEventListener('click', handler);
	});
}

// 現在の習慣に対する振り返り内容（成功/失敗・質問回答）を保存する
function recordUserReview() {
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
}

// 振り返り完了時のUI表示と送信処理を担当
function finishReviewFlow() {
	showReviewCompletionMessage();
	prepareSubmitButton();
}

function showReviewCompletionMessage() {
	document.getElementById("modalHabitLabel").textContent = "すべての振り返りが完了しました！";
	document.querySelector(".result-options").style.display = "none";
	document.getElementById("questionBlock").style.display = "none";
}

function prepareSubmitButton() {
	document.getElementById("navigationButtons").innerHTML = '<button id="submitButton">送信する</button>';
	document.getElementById("submitButton").addEventListener('click', submitReviewResults);
}

async function submitReviewResults() {
	try {
		const response = await fetch('/api/review/records', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(reviewResults)
		});

		if (!response.ok) {
			throw new Error('送信に失敗しました');
		}

		alert("振り返りを送信しました！");
		console.log("送信するデータ:", reviewResults);
		closeModalAndResetState();
	} catch (error) {
		console.error("送信エラー:", error);
		alert("送信に失敗しました。もう一度お試しください。");
	}

}

function closeModalAndResetState() {
	modal.style.display = "none";
	reviewResults = [];
	currentHabitIndex = 0;
}

// モーダル表示を初期化する関数
function resetModalState() {
	// ラジオボタンのチェック解除
	document.querySelectorAll('input[name="result"]').forEach(radio => radio.checked = false);
	// 質問・ナビボタン非表示
	document.getElementById("questionBlock").style.display = "none";
	document.getElementById("navigationButtons").style.display = "none";
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
