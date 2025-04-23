var modal = document.getElementById("myModal");
const startButton = document.getElementById("startButton");
var span = document.getElementsByClassName("close")[0];
let activeHabits = [];

// ページ読み込み時に習慣を取得
window.addEventListener('DOMContentLoaded', function() {
	// APIから習慣一覧を取得
	fetchActiveHabits();
});

// スタートボタンをクリックした時にモーダルを表示
startButton.onclick = function() {
	modal.style.display = "block";

}

// APIからユーザーのアクティブな習慣を取得する関数
function fetchActiveHabits() {
	// ユーザーIDを取得（実際の実装に合わせて調整）
	const userId = 1; // 例として固定値を使用

	// APIリクエスト
	fetch(`/api/habits?userId=${userId}`)
		.then(response => response.json())
		.then(habits => {
			//グローバル変数に格納
			activeHabits = habits;
			// 習慣のラベルをモーダルに表示
			displayHabits(habits);
			// ホーム画面に習慣のラベルと説明を表示
			displayHabitsOnHome(habits);

		})
		.catch(error => {
			console.error('習慣データの取得に失敗:', error);
		});
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

// 習慣をモーダルに表示する関数
function displayHabits(habits) {
	// 習慣リスト用の要素を作成
	const habitsList = document.createElement('ul');
	habitsList.className = 'habits-list';

	// 各習慣のラベルをリストに追加
	habits.forEach(habit => {
		const listItem = document.createElement('li');
		listItem.textContent = habit.label;
		habitsList.appendChild(listItem);
	});

	// モーダルのコンテンツ要素を取得
	const modalContent = document.querySelector('.modal-content');

	// 既存のラジオボタンの後にリストを挿入
	modalContent.appendChild(habitsList);
}


// モーダルの×ボタンをクリックした時にモーダルを閉じる
span.onclick = function() {
	modal.style.display = "none";
}

// モーダルの外をクリックした時にモーダルを閉じる
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}
