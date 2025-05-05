//外部モジュール
import { fetchActiveHabits } from './api/habit.js';

let activeHabits = [];

//**イベント */

// ページ読み込み時
window.addEventListener('DOMContentLoaded', async function() {
	// APIから習慣一覧を取得
	//Promiseの完了を待って中身を取得
	const habits = await fetchActiveHabits(1);
	//グローバル変数に格納
	activeHabits = habits;
	// ホーム画面に習慣のラベルと説明を表示
	displayHabitsOnHome(habits);
});


//**関数 */

// モーダルに習慣名を表示する関数
function showHabitOnModal(index) {
	const habit = activeHabits[index];
	console.log(habit);
	document.getElementById("modalHabitLabel").textContent = habit.label;
}

// ホーム画面に習慣を表示する関数
function displayHabitsOnHome(habits) {
	const container = document.getElementById('homeHabitsContainer');
	container.innerHTML = ''; // 一旦クリア
	console.log(habits);
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

