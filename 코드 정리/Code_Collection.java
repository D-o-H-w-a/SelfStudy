// 2025년 01월 23일 자 코드정리

// 1차 팀프로젝트 코드

// SalaryMapper.java

// Get으로 데이터 맵핑 요청 시 같이 가져온 Param 값들을 통해 데이터 시작위치와 데이터 개수를 받아온 다음
// 해당 갯수만큼의 데이터를 가져온 후 조회창으로 넘어가도록 구현
// @GetMapping("salarylist")
//	public String salarylist(@RequestParam(defaultValue = "0") int start, // 가져올 데이터의 시작 위치
//	        @RequestParam(defaultValue = "5") int limit, // 한 번에 가져올 데이터 개수
//	        Model model){
//
//		int listLimit = 5;	// 한번에 보여줄 데이터 갯수
//
//		// 전체 데이터 갯수 [그리드로 처리할거면 굳이 전체 데이터 갯수를 찾아야하나..?]
//		// int listCount = boardService.getBoardListCount();
//
//		// 데이터 가져오기
//		// List<SalaryInfo> salaryList = SalaryService.getSalaryList(start, limit);
//
//	    // View에 데이터를 전달
//	    // model.addAttribute("salaryList", salaryList);
//
//		return "salary/salary_list";
//	}

// 조회 프로그램 Service 에서 List 형태의 SalaryVo 데이터를 반환해줄 함수 [파라미터는 기준값과 최대값을 int로 선언]
// public List<SalaryVO> getSalaryList(int start, int limit) {
//		return mapper.getSalaryList(start, limit);
//	}

// 조회 프로그램 Mapper 에서 기준값과 최대값을 가지고 조회 데이터를 가져오기 위한  추상 메소드
// public List<SalaryVO> getSalaryList(@Param("start")int start, @Param("list")int limit);

