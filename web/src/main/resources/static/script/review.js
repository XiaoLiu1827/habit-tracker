/**
 * 振り返り機能に関連した処理
 */

import { fetchActiveHabits } from './api/habit.js';

var modal = document.getElementById("myModal");
let activeHabits = [];
let currentHabitIndex = 0;
let reviewResults = [];
let questionStoreMap = new Map();
const reviewDate = getReviewDate();
const startButton = document.getElementById("startButton");
const closeButton = document.querySelector(".close");



//イベント
document.addEventListener('DOMContentLoaded', async () => {
	await checkReviewSessionStatus();
});

// スタートボタンをクリックした時にモーダルを表示
startButton.onclick = async function() {
	const habits = await fetchActiveHabits(1);
	if (habits.length === 0) {
		alert('習慣が登録されていません');
		return;
	}

	activeHabits = habits;
	currentHabitIndex = 0; // 最初の習慣からスタート
	await fetchQuestions();
	//習慣名をモーダルに表示する
	handleModalFlow();
}

closeButton.addEventListener("click", () => {
	const shouldClose = confirm("振り返りを中断しますか？");
	if (shouldClose) {
		closeModalAndResetState();
	}
});

//関数

function getReviewDate() {
	const now = new Date();
	const reviewStartHour = 22; // TODO: ユーザー設定に合わせて動的に変更

	//設定時間より現在時刻が早い場合、振り返り対象を前日とみなす
	const currentHour = now.getHours();
	if (currentHour < reviewStartHour) {
		now.setDate(now.getDate() - 1);
	}

	// ISO形式のYYYY-MM-DDを返す
	// 例: "2025-05-04T14:21:00.000Z"
	// → "2025-05-04"
	return now.toISOString().split('T')[0];
}

async function checkReviewSessionStatus() {
	try {
		const response = await fetch(`/api/review/session/status?reviewDate=${reviewDate}`);
		if (!response.ok) {
			throw new Error('ステータス取得失敗');
		}

		const data = await response.json();

		if (data.reviewed) {
			showCompletedMessage(reviewDate);
		} else {
			showReviewStartButton(reviewDate);
		}

	} catch (e) {
		console.error('振り返り判定エラー:', e);
		document.getElementById('review-status-message').textContent = 'エラーが発生しました。';
	}
}

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


//質問をカテゴリごとに分類
function storeQuestionsByCategory(questions) {
	//連想配列（object）を2重配列（[キー, 値] の配列の配列）に変換してから格納する
	Object.entries(questions).forEach(([categoryKey, questionList]) => {
		questionStoreMap.set(categoryKey, questionList);
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
		const response = await fetch(`/api/review/records?reviewDate=${reviewDate}`, {
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
		showCompletedMessage();
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

function showCompletedMessage() {
	const container = document.getElementById('review-container');
	startButton.style.display = 'none';
	container.innerHTML = `<p>本日（${reviewDate}）の振り返りは完了しています。</p>`;
}

function showReviewStartButton() {
	startButton.style.display = 'block';

}
