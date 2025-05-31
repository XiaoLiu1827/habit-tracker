/**
 * 習慣関連の処理
 */

// APIからユーザーのアクティブな習慣を取得する関数
export async function fetchActiveHabits(userId) {
	try {
		const response = await fetch(`/api/habits?userId=${userId}`);
		if (!response.ok) {
			throw new Error(`APIエラー: ${response.status}`);
		}
		return await response.json();
	} catch (error) {
		console.error('習慣データの取得に失敗:', error);
		return [];
	}
}