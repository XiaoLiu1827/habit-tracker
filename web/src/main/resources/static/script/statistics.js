//外部モジュール
import { fetchActiveHabits } from './api/habit.js';

let cachedHabits = [];

// API呼び出しはスクリプト実行直後に開始し、非同期でデータを取得しておく
// 他の初期処理に影響を与えないよう、ここでは await せず Promise を作成しておく
// データ取得後は cachedHabits に格納される（表示は DOMContentLoaded 後に実行）
const fetchPromise = fetchActiveHabits(1).then(data => {
	cachedHabits = data;
});

// 成功率データのキャッシュ
const successRateCache = {};// { habitId: 結果 }

//**イベント */

// ページ読み込み時
window.addEventListener('DOMContentLoaded', async () => {
	//cachedHabitsの格納を待機して表示処理に移行
	await fetchPromise;

	// 習慣ボタンのコンテナ
	const buttonContainer = document.getElementById("habitButtons");

	const successRateContent = document.getElementById("successRateContent");

	cachedHabits.forEach(habit => {
		const button = document.createElement("button");
		button.className = "habit-button";
		button.textContent = habit.label;
		button.dataset.habitId = habit.id;

		button.addEventListener("click", async () => {
			const habitId = habit.id;

			//該当データがキャッシュ済みであればそのまま使用する
			if (successRateCache[habitId]) {
				const stat = successRateCache[habitId];
				successRateContent.textContent = `成功率：${stat.successRate}%（${stat.successCount}/${stat.totalCount}日）`;
				return;
			}

			//キャッシュがなければAPIをたたく
			try {
				const now = new Date();
				const year = now.getFullYear();
				const month = now.getMonth() + 1;

				const res = await fetch(`/api/statistics/success-rate?habitId=${habitId}&year=${year}&month=${month}`);
				if (!res.ok) throw new Error("データ取得失敗");

				const stat = await res.json();

				// キャッシュに保存
				successRateCache[habitId] = stat;

				successRateContent.textContent = `成功率：${stat.successRate}%（${stat.successCount}/${stat.totalCount}日）`;
			} catch (error) {
				console.error("取得エラー", error);
				successRateContent.textContent = "統計データの取得に失敗しました";
			}
		});

		buttonContainer.appendChild(button);
	});
});