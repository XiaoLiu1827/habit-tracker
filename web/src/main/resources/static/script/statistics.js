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
const habitStatisticsCache = {};// { habitId: 結果 }

//**イベント */

// ページ読み込み時
window.addEventListener('DOMContentLoaded', async () => {
	//cachedHabitsの格納を待機して表示処理に移行
	await fetchPromise;

	// 習慣ボタンのコンテナ
	const buttonContainer = document.getElementById("habitButtons");
	const successRateContent = document.getElementById("successRateContent");
	const streakContent = document.getElementById("streakContent");
	const questionStatsContent = document.getElementById("questionStatsContent");


	cachedHabits.forEach(habit => {
		const button = document.createElement("button");
		button.className = "habit-button";
		button.textContent = habit.label;
		button.dataset.habitId = habit.id;

		button.addEventListener("click", async () => {
			const habitId = habit.id;

			// 全ボタンの選択状態をリセット
			document.querySelectorAll(".habit-button").forEach(btn => {
				btn.classList.remove("selected");
			});

			// このボタンだけ選択状態にする
			button.classList.add("selected");

			//該当データがキャッシュにあればそのまま使用
			if (habitStatisticsCache[habitId]) {
				const stat = habitStatisticsCache[habitId];
				const rate = stat.successRateDto;
				const streak = stat.streakDto;
				const questionStats = stat.questionStatList;

				successRateContent.textContent = `成功率：${rate.successRate}%（${rate.successCount}/${rate.totalCount}日）`;
				streakContent.textContent = `${streak.streakDays}日連続達成中！`
				renderQuestionStats(questionStats);
				console.log(`統計表示中（キャッシュ）：習慣ID=${habit.id}`);
				return;
			}

			//キャッシュがなければAPIをたたく
			try {
				const now = new Date();
				const year = now.getFullYear();
				const month = now.getMonth() + 1;

				const res = await fetch(`/api/statistics?habitId=${habitId}&year=${year}&month=${month}`);
				if (!res.ok) throw new Error("データ取得失敗");

				const stat = await res.json();

				// キャッシュに保存
				habitStatisticsCache[habitId] = stat;

				const rate = stat.successRateDto;
				const streak = stat.streakDto;
				const questionStats = stat.questionStatList;

				successRateContent.textContent = `成功率：${rate.successRate}%（${rate.successCount}/${rate.totalCount}日）`;
				streakContent.textContent = `${streak.streakDays}日連続達成中！`
				renderQuestionStats(questionStats);
				console.log(`統計表示中（api）：習慣ID=${habit.id}`);

			} catch (error) {
				console.error("取得エラー", error);
				successRateContent.textContent = "統計データの取得に失敗しました";
			}
		});

		buttonContainer.appendChild(button);
	});
});

//**関数 */

function renderQuestionStats(questionStats) {
	questionStatsContent.innerHTML = ""; // 前回表示分をリセット

	questionStats.forEach(stat => {
		const questionBlock = document.createElement("div");
		questionBlock.className = "question-block";

		const title = document.createElement("h4");
		title.textContent = stat.questionLabel;
		questionBlock.appendChild(title);

		const ul = document.createElement("ul");
		stat.choices.forEach(choice => {
			const li = document.createElement("li");
			li.textContent = `${choice.choiceLabel}：${choice.answerCount}件`;
			ul.appendChild(li);
		});

		questionBlock.appendChild(ul);
		questionStatsContent.appendChild(questionBlock);
	});
}
